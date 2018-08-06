//package com.example.dao;
//
//        import com.example.entity.Activity;
//        import com.example.entity.Coupon;
////        import org.apache.ibatis.annotations.Insert;
////        import org.apache.ibatis.annotations.Mapper;
////        import org.apache.ibatis.annotations.Param;
////        import org.apache.ibatis.annotations.Select;
//        import org.springframework.stereotype.Repository;
//
///**
// *
// */
//@Repository
////@Mapper
//public interface CouponDao {
//
//    public int insert(Coupon coupon) throws Exception;
//
//    public Coupon getLastOne() throws Exception;
//
//    public int insertActivity(String activityId) throws  Exception;
//
//    public Activity getLastActivity() throws Exception;
//
//    //@Param("activit_id") String activityId, @Param("buyer_id") String buyerId,
//    //    @Param("merchant_id") String merchantId, @Param("prize_id") String prize_id
////    @Insert("insert into coupon (" +
////            "out_biz_no,activity_id,buyer_id,merchant_uid,prize_id) values(#{c.outBizNo},#{c.activityId},#{c.buyerId},#{c.merchantUid},#{c.prizeId})")
////    public int insert(@Param("c") Coupon coupon) throws Exception;
////
////    @Select({ "select id, activity_id as activityId,buyer_id as buyerId,merchant_uid as merchantUid,prize_id as prizeId  from coupon order by id desc ", "limit 0,1"})
////    public Coupon getLastOne() throws Exception;
////
////    @Insert("insert into activity (activity_id) values (#{activityId})")
////    public int insertActivity(String activityId) throws  Exception;
////
////    @Select({"select id, activity_id as activityId from activity order by id desc ","limit 0,1"})
////    public Activity getLastActivity() throws Exception;
//}