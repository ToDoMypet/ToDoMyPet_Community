package com.todomypet.communityservice.service;

import com.todomypet.communityservice.dto.ect.ReportDTO;
import com.todomypet.communityservice.exception.CustomException;
import com.todomypet.communityservice.exception.ErrorCode;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    @Autowired
    private final JavaMailSender javaMailSender;

    public void sendReportMail(ReportDTO reportInfo) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo("todomypet@gmail.com");
        helper.setSubject("[To-do My Pet] 게시글 신고 접수");

        String htmlContent = "신고 접수 유형: " + reportInfo.getReportType().toString() + "<br><br>";
        htmlContent += "신고인 Id: " + reportInfo.getReporterId() + "<br><br>";
        htmlContent += "신고된 콘텐츠: (이미지 uri)" + reportInfo.getReportedUri() +
                " (내용)" + reportInfo.getReportedContent() + " (고유번호)" + reportInfo.getReportedId() + "<br><br>";
        htmlContent += "작성자 닉네임: " + reportInfo.getWriterNickname()
                + " / 고유번호: " + reportInfo.getWriterId() + "<br><br>";

        helper.setText(htmlContent, true);

        helper.setFrom(new InternetAddress("todomypet@gmail.com", "To-do My Pet"));

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.MAIL_SEND_FAIL);
        }
    }
}
