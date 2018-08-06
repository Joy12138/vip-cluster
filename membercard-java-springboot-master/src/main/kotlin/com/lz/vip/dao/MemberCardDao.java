//package com.example.dao;
//
//import com.example.entity.CardTemplate;
//import com.example.entity.MemberCard;
////import org.apache.ibatis.annotations.Insert;
////import org.apache.ibatis.annotations.Mapper;
////import org.apache.ibatis.annotations.Param;
////import org.apache.ibatis.annotations.Select;
//import org.springframework.stereotype.Repository;
//
///**
// *
// */
//@Repository
////@Mapper
//public interface MemberCardDao {
//
//    //@Param("activit_id") String activityId, @Param("buyer_id") String buyerId,
//    //    @Param("merchant_id") String merchantId, @Param("prize_id") String prize_id
////    @Insert("insert into " + table
////            + " (alipay_card_no,external_card_no,alipay_user_id,card_template_id,schema_url) values(#{c.alipayCardNo},#{c.externalCardNo},#{c.alipayUserId},#{c.cardTemplateId},#{c.schemaUrl})")
////    public int insert(@Param("c") MemberCard card) throws Exception;
////
////    @Select({
////            "select id, alipay_card_no as alipayCardNo,external_card_no as externalCardNo,alipay_user_id as alipayUserId,card_template_id as cardTemplateId ,schema_url as schemaUrl  from ",
////            table, " order by id desc ", "limit 0,1" })
////    public MemberCard getLastOne() throws Exception;
////
////    @Insert("insert into card_template (card_template_id) values (#{templateId})")
////    public int insertTemplate(String templateId) throws Exception;
////
////    @Select({ "select id, card_template_id as cardTemplateId from ",
////              "card_template order by id desc ", "limit 0,1" })
////    public CardTemplate getLastTemplate() throws Exception;
//}