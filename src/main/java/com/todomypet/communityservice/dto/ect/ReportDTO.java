package com.todomypet.communityservice.dto.ect;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportDTO {
    private ReportType reportType;
    private String reporterId;
    private String reporterEmail;
    private String reporterNickname;
    private String reportedId;
    private String reportedUri;
    private String reportedContent;
    private String writerId;
    private String writerEmail;
    private String writerNickname;
}
