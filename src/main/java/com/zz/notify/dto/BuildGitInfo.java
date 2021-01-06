package com.zz.notify.dto;

/**
 * @(#) BuildGitInfo 1.0 2020/3/16
 * @author: huangjd
 */
public class BuildGitInfo {

    private String gitCommit;

    private String gitCommitterName;

    private String gitAuthorName ;

    private String gitAuthorEmail;

    private String gitCommitterEmail ;

    private String gitUrl;

    private String gitBranch;

    private String gitLocalBranch;

    private String gitPreviousCommit;

    private String gitPreviousSuccessfulCommit;

    private String changes;


    public String getGitCommit() {
        return gitCommit;
    }

    public void setGitCommit(String gitCommit) {
        this.gitCommit = gitCommit;
    }

    public String getGitCommitterName() {
        return gitCommitterName;
    }

    public void setGitCommitterName(String gitCommitterName) {
        this.gitCommitterName = gitCommitterName;
    }

    public String getGitAuthorName() {
        return gitAuthorName;
    }

    public void setGitAuthorName(String gitAuthorName) {
        this.gitAuthorName = gitAuthorName;
    }

    public String getGitAuthorEmail() {
        return gitAuthorEmail;
    }

    public void setGitAuthorEmail(String gitAuthorEmail) {
        this.gitAuthorEmail = gitAuthorEmail;
    }

    public String getGitCommitterEmail() {
        return gitCommitterEmail;
    }

    public void setGitCommitterEmail(String gitCommitterEmail) {
        this.gitCommitterEmail = gitCommitterEmail;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getGitBranch() {
        return gitBranch;
    }

    public void setGitBranch(String gitBranch) {
        this.gitBranch = gitBranch;
    }

    public String getGitLocalBranch() {
        return gitLocalBranch;
    }

    public void setGitLocalBranch(String gitLocalBranch) {
        this.gitLocalBranch = gitLocalBranch;
    }

    public String getGitPreviousCommit() {
        return gitPreviousCommit;
    }

    public void setGitPreviousCommit(String gitPreviousCommit) {
        this.gitPreviousCommit = gitPreviousCommit;
    }

    public String getGitPreviousSuccessfulCommit() {
        return gitPreviousSuccessfulCommit;
    }

    public void setGitPreviousSuccessfulCommit(String gitPreviousSuccessfulCommit) {
        this.gitPreviousSuccessfulCommit = gitPreviousSuccessfulCommit;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }
}
