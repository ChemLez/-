package cn.lizhi.serviceOrder.service;

import cn.lizhi.serviceOrder.entity.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author liz
 * @since 2021-08-21
 */
public interface TOrderService extends IService<TOrder> {

    String createOrders(String courseId, String memberId);

    TOrder getOrderByOrderId(String orderId);

    boolean isByCourse(String memberId, String orderId);
}
