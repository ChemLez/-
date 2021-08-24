package cn.lizhi.serviceOrder.service.impl;

import cn.lizhi.commonUtils.orderVo.CourseWebVoOrder;
import cn.lizhi.commonUtils.orderVo.UcenterMemberOrder;
import cn.lizhi.serviceOrder.client.EduClient;
import cn.lizhi.serviceOrder.client.UcenterClient;
import cn.lizhi.serviceOrder.entity.TOrder;
import cn.lizhi.serviceOrder.mapper.TOrderMapper;
import cn.lizhi.serviceOrder.service.TOrderService;
import cn.lizhi.serviceOrder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author liz
 * @since 2021-08-21
 */
@Service(value = "orderService")
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    /**
     * 订单中包含 讲师信息和课程信息、用户信息 需要通过远程调用 来获取这两部分的信息
     *
     * @param courseId 课程id
     * @param memberId 用户id
     * @return
     */
    @Override
    public String createOrders(String courseId, String memberId) {

        CourseWebVoOrder courseInfo = (CourseWebVoOrder) eduClient.getCourseInfo(courseId).getData().get("item"); // 课程相关信息

        UcenterMemberOrder member = ucenterClient.getUcenterMemberById(memberId); // 查询出的用户信息

        // 开始时状态模式为0 - 代表为支付的订单
        TOrder order = new TOrder(); // 创建订单实体

        String orderNo = OrderNoUtil.getOrderNo(); // 生成订单号
        order.setOrderNo(orderNo); // 设置订单号
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfo.getTitle());
        order.setCourseCover(courseInfo.getCover());
        order.setTeacherName("test");
        order.setTotalFee(courseInfo.getPrice());
        order.setMemberId(memberId);
        order.setMobile(member.getMobile());
        order.setNickname(member.getNickname());
        order.setStatus(0);
        order.setPayType(1);
        baseMapper.insert(order);
        return orderNo;
    }

    @Override
    public TOrder getOrderByOrderId(String orderId) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderId);
        TOrder order = baseMapper.selectOne(wrapper);
        return order;
    }

    @Override
    public boolean isByCourse(String memberId, String orderId) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", memberId);
        wrapper.eq("order_no", orderId);
        Integer count = baseMapper.selectCount(wrapper);
        return count != null && count > 0;
    }
}
