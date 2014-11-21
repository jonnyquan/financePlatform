package com.sunlights.account.web;


import com.sunlights.account.service.ActivityService;
import com.sunlights.account.service.impl.ActivityServiceImpl;
import com.sunlights.account.service.rewardrules.IObtainRewardRule;
import com.sunlights.account.service.rewardrules.RewardRuleFactory;
import com.sunlights.account.vo.ActivityParamter;
import com.sunlights.account.vo.ActivityVo;
import com.sunlights.account.vo.ObtainRewardVo;
import com.sunlights.account.vo.RewardResultVo;
import com.sunlights.common.MsgCode;
import com.sunlights.common.Severity;
import com.sunlights.common.vo.Message;
import com.sunlights.common.vo.PageVo;
import models.CustomerSession;
import play.Logger;
import play.db.jpa.Transactional;
import play.mvc.Result;

import java.util.List;

/**
 * Created by tangweiqun on 2014/11/13.
 */
@Transactional
public class ActivityController extends ActivityBaseController  {


    private ActivityService activityService = new ActivityServiceImpl();


    public Result getActivityList() {
        ActivityParamter activityParamter = getActivityParamter();
        PageVo pageVo = new PageVo();
        pageVo.setIndex(activityParamter.getIndex());
        pageVo.setPageSize(activityParamter.getPageSize());

        List<ActivityVo> activityVos = activityService.getActivityVos(pageVo);

        pageVo.setList(activityVos);
        if(activityVos != null) {
            pageVo.setCount(activityVos.size());
        }
        messageUtil.setMessage(new Message(Severity.INFO, MsgCode.OPERATE_SUCCESS), pageVo);

        Logger.debug("获取活动的信息：" + messageUtil.toJson().toString());
        return ok(messageUtil.toJson());
    }


    /**
     * 用户获取奖励接口
     *
     * @return
     */
    public Result obtainReward() {
        //1：获取请求参数
        String token = getToken();
        ActivityParamter activityParamter = getActivityParamter();

        //2:获取获取奖励需要的参数
        CustomerSession customerSession = customerService.getCustomerSession(token);
        String custNo = customerSession.getCustomerId();
        String scene = activityParamter.getScene();

        //3:获取相对应场景的奖励获取规则的处理类
        IObtainRewardRule iObtainRewardRule = RewardRuleFactory.getIObtainRuleHandler(scene);
        ObtainRewardVo obtainRewardVo = new ObtainRewardVo();
        if(iObtainRewardRule == null) {
            Logger.info("还没有配置签到的场景");
            messageUtil.setMessage(new Message(Severity.INFO, MsgCode.NOT_CONFIG_ACTIVITY_SCENE), obtainRewardVo);
            return ok(messageUtil.toJson());
        }
        //4:处理获取奖励
        RewardResultVo rewardResultVo = iObtainRewardRule.obtainReward(custNo);

        //5:解析结果并发往客户端
        Message returnMessage = rewardResultVo.getReturnMessage();
        if(MsgCode.OBTAIN_SUCC.getCode().equals(returnMessage.getCode())) {
            obtainRewardVo.setScene(scene);
            obtainRewardVo.setObtainReward(rewardResultVo.getRewards());
            obtainRewardVo.setCanNotObtain(true);
        }
        messageUtil.setMessage(returnMessage, obtainRewardVo);
        return ok(messageUtil.toJson());
    }

}