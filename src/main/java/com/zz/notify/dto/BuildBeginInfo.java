package com.zz.notify.dto;

import com.zz.notify.NotificationUtil;
import com.zz.notify.model.NotificationConfig;
import hudson.model.AbstractBuild;
import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.scm.ChangeLogSet;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 开始构建的通知信息
 * @author jiaju
 */
public class BuildBeginInfo {

    /**
     * 请求参数
     */
    private Map params = new HashMap<String, Object>();

    /**
     * 预计时间，毫秒
     */
    private Long durationTime = 0L;

    /**
     * 本次构建控制台地址
     */
    private String consoleUrl;

    /**
     * 工程名称
     */
    private String projectName;

    /**
     * 环境名称
     */
    private String topicName = "";

    /**
     * 构建编号
     */
    private Integer buildNumber;


    /**
     * 触发原因
     */
    private String triggerCause;



    /**
     * 更新日志
     */
    private String changeLog;

    /**
     * 更新日志URL
     */
    private String changeLogUrl;

    public static final int WECOM_LOG_LIMIT = 3048;

    public BuildBeginInfo(String projectName, AbstractBuild<?, ?> build, NotificationConfig config){
        //获取请求参数
        List<ParametersAction> parameterList = build.getActions(ParametersAction.class);
        if(parameterList!=null && parameterList.size()>0){
            for(ParametersAction p : parameterList){
                for(ParameterValue pv : p.getParameters()){
                    this.params.put(pv.getName(), pv.getValue());
                }
            }
        }
        //预计时间
        if(build.getProject().getEstimatedDuration()>0){
            this.durationTime = build.getProject().getEstimatedDuration();
        }
        //控制台地址
        StringBuilder urlBuilder = new StringBuilder();
        String jenkinsUrl = NotificationUtil.getJenkinsUrl();
        if(StringUtils.isNotEmpty(jenkinsUrl)){
            String buildUrl = build.getUrl();
            urlBuilder.append(jenkinsUrl);
            if(!jenkinsUrl.endsWith("/")){
                urlBuilder.append("/");
            }
            StringBuilder projectUrl = new StringBuilder(urlBuilder).append(build.getProject().getUrl());
            if(!build.getProject().getUrl().endsWith("/")){
                projectUrl.append("/");
            }
            this.changeLogUrl = projectUrl.append("changes").toString();
            urlBuilder.append(buildUrl);
            if(!buildUrl.endsWith("/")){
                urlBuilder.append("/");
            }
            this.consoleUrl = new StringBuilder(urlBuilder).append("console").toString();
        }

        //工程名称
        this.projectName = projectName;
        //环境名称
        if(config.topicName!=null){
            topicName = config.topicName;
        }
        this.buildNumber = build.number;
//        this.changeLogUrl = new StringBuilder(urlBuilder).append("changes").toString();
        this.changeLog = buildChangeSet(build.getChangeSet());
        this.triggerCause =  build.getCauses().get(0).getShortDescription();
    }

    public String toJSONString(BuildGitInfo buildGitInfo){
        //参数组装
        StringBuffer paramBuffer = new StringBuffer();
        params.forEach((key, val)->{
            paramBuffer.append(key);
            paramBuffer.append("=");
            paramBuffer.append(val);
            paramBuffer.append(", ");
        });
        if(paramBuffer.length()==0){
            paramBuffer.append("无");
        }else{
            paramBuffer.deleteCharAt(paramBuffer.length()-2);
        }

        //耗时预计
        String durationTimeStr = "无";
        if(durationTime>0){
            Long l = durationTime / (1000 * 60);
            durationTimeStr = l + "分钟";
        }

        //组装内容
        StringBuilder content = new StringBuilder();
        if(StringUtils.isNotEmpty(topicName)){
            content.append(this.topicName);
        }
        content.append("<font color=\"info\">【" + this.projectName + "】</font>开始第");
        content.append(this.buildNumber).append("次构建\n");
        content.append(" >构建参数：<font color=\"comment\">" + paramBuffer.toString() + "</font>\n");
        content.append(" >预计用时：<font color=\"comment\">" +  durationTimeStr + "</font>\n");
        content.append(" >触发原因：<font color=\"comment\">").append(this.triggerCause).append("</font>\n");
        if(buildGitInfo!=null){
            content.append(gitContent(buildGitInfo));
        }
        if(StringUtils.isNotEmpty(this.consoleUrl)){
            content.append(" >[查看控制台](" + this.consoleUrl ).append(")");
        }

        Map markdown = new HashMap<String, Object>();
        markdown.put("content", content.toString());

        Map data = new HashMap<String, Object>();
        data.put("msgtype", "markdown");
        data.put("markdown", markdown);

        String req = JSONObject.fromObject(data).toString();
        return req;
    }


    public  String gitContent(BuildGitInfo buildGitInfo){
        String printContent = StringUtils.isBlank(this.changeLog)?"没有任何更新"
                : this.changeLog.substring(0,this.changeLog.length() > WECOM_LOG_LIMIT? WECOM_LOG_LIMIT:this.changeLog.length());
        StringBuilder content = new StringBuilder();
        content.append(" >GIT HASH:<font color=\"comment\">").append(buildGitInfo.getGitCommit()).append("</font>\n");
        content.append(" >此次更新内容：");
        content.append("<font color=\"comment\">").append(printContent).append("</font>\n");
        content.append(" > [查看更多更新内容]("+this.changeLogUrl).append(")\n");
        return content.toString();
    }


    private String buildChangeSet(ChangeLogSet changeLogSet) {
        if (changeLogSet.getItems().length == 0) {
            return null;
        }
        StringBuilder changes = new StringBuilder();
        for (Iterator<? extends ChangeLogSet.Entry> i = changeLogSet.iterator(); i.hasNext(); ) {
            ChangeLogSet.Entry change = i.next();
            changes.append("\n");
            changes.append(change.getMsg());
            changes.append(" - ");
            changes.append(change.getAuthor());
        }
        return changes.toString();
    }







}
