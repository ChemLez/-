package cn.lizhi.serviceOrder.service.impl;

import cn.lizhi.serviceBase.exception.GuliException;
import cn.lizhi.serviceOrder.entity.TOrder;
import cn.lizhi.serviceOrder.entity.TPayLog;
import cn.lizhi.serviceOrder.mapper.TPayLogMapper;
import cn.lizhi.serviceOrder.service.TPayLogService;
import cn.lizhi.serviceOrder.utils.HttpClient;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.wxpay.sdk.WXPayUtil;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author liz
 * @since 2021-08-21
 */
@Service(value = "payLogService")
public class TPayLogServiceImpl extends ServiceImpl<TPayLogMapper, TPayLog> implements TPayLogService {

    @Autowired
    private TOrderServiceImpl orderService;


    private void testCreateNative(String orderNo) {
        try {
            //1 根据订单号查询订单信息
            QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderNo);
            TOrder order = orderService.getOne(wrapper);

            PrivateKey privateKey = (PrivateKey) PemUtil.loadCertificate(new ByteArrayInputStream("T6m9iK73b0kn9g5v426MKfHQH7X8rKwb".getBytes()));
            CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
                    .withMerchant("1558950191", "wx74862e0dfcf69954", privateKey)
                    .withValidator(response -> true) // NOTE: 设置一个空的应答签名验证器，**不要**用在业务请求
                    .build();

            // 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签
            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
            httpPost.addHeader("Accept", "application/json");
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectMapper objectMapper = new ObjectMapper();

            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("mchid", "1558950191")
                    .put("appid", "wx74862e0dfcf69954")
                    .put("description", order.getCourseTitle())
                    .put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n")
                    .put("out_trade_no", orderNo);
            rootNode.putObject("amount")
                    .put("total", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");
            rootNode.putObject("payer")
                    .put("openid", "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");
            objectMapper.writeValue(bos, rootNode);
            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String bodyAsString = EntityUtils.toString(response.getEntity());
            System.out.println(bodyAsString);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public Map createNative(String orderNo) {
        try {
            QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderNo);
            TOrder order = orderService.getOne(wrapper);

            //2 使用map设置生成二维码需要的参数 - appid、mch_id、加密
            Map m = new HashMap();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getCourseTitle()); //课程标题
            m.put("out_trade_no", orderNo); //订单号
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("1")).longValue() + "");
            m.put("spbill_create_ip", "127.0.0.1");
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            m.put("trade_type", "NATIVE");

            //3 发送httpclient请求，传递参数xml格式，微信支付提供的固定的地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //设置xml格式的参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            //执行post请求发送
            client.post();

            //4 得到发送请求返回结果
            //返回内容，是使用xml格式返回
            String xml = client.getContent();

            System.out.println(xml);
            //把xml格式转换map集合，把map集合返回
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            //最终返回数据 的封装
            Map map = new HashMap();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));  //返回二维码操作状态码
            map.put("code_url", resultMap.get("code_url"));        //二维码地址

            return map;
        } catch (Exception e) {
            throw new GuliException(20001, "生成二维码失败");
        }

    }

    /**
     * 查询订单支付状态 - 是否支付成功
     *
     * @param orderNo
     * @return
     */
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            // 封装参数
            Map<String, String> m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954"); // 公众账号
            m.put("mch_id", "1558950191"); // 商户号
            m.put("out_trade_no", orderNo); // 订单号
            m.put("nonce_str", WXPayUtil.generateNonceStr()); // 随机字符串


            //2 发送httpclient - 模拟浏览器发送请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb")); // 设置签名
            client.setHttps(true);
            client.post();

            //3 得到请求返回内容
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            System.out.println(resultMap);
            //6、转成Map再返回
            return resultMap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 支付成功后，更改订单状态
     *
     * @param map
     */
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        // 获取订单号
        String orderNo = map.get("out_trade_no");

        // 查询出订单信息
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        TOrder order = orderService.getOne(wrapper);
        if (order.getStatus().intValue() == 1) { // 订单已经更新了
            return;
        }
        order.setStatus(1);
        orderService.updateById(order);

        // 向支付表中添加支付记录
        TPayLog payLog = new TPayLog();

        payLog.setOrderNo(orderNo);  //订单号
        payLog.setPayTime(new Date()); //订单完成时间
        payLog.setPayType(1);//支付类型 1微信
        payLog.setTotalFee(order.getTotalFee());//总金额(分)

        payLog.setTradeState(map.get("trade_state"));
        payLog.setTransactionId(map.get("transaction_id")); //流水号 - 由微信端进行返回
        payLog.setAttr(JSONObject.toJSONString(map)); // 整个微信查询订单的详情
        baseMapper.insert(payLog);
    }


}
