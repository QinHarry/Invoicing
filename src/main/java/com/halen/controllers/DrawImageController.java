package com.halen.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Controller
public class DrawImageController {

    public static final int WIDTH = 120;
    public static final int HEIGHT = 30;

    @RequestMapping("/drawImage")
    public void drawImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        setBackGround(g);
        setBorder(g);
        drawRandomLine(g);
        String random = drawRandomNum((Graphics2D) g);
        request.getSession().setAttribute("checkcode", random);
        response.setContentType("image/jpeg");
        response.setDateHeader("expires", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        ImageIO.write(bi, "jpg", response.getOutputStream());
    }

    private void setBackGround(Graphics g) {
        // 设置颜色
        g.setColor(new Color(22, 160, 133));
        // 填充区域
        g.fillRect(0, 0, WIDTH, HEIGHT);

    }

    private void setBorder(Graphics g) {
        // 设置边框颜色
        g.setColor(new Color(22, 160, 133));
        // 边框区域
        g.drawRect(1, 1, WIDTH - 2, HEIGHT - 2);
    }

    private void drawRandomLine(Graphics g) {
        // 设置颜色
        g.setColor(Color.WHITE);
        // 设置线条个数并画线
        for (int i = 0; i < 5; i++) {
            int x1 = new Random().nextInt(WIDTH);
            int y1 = new Random().nextInt(HEIGHT);
            int x2 = new Random().nextInt(WIDTH);
            int y2 = new Random().nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

    }

    private String drawRandomNum(Graphics2D g) {
        StringBuffer sb = new StringBuffer();
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimeRoman", Font.BOLD, 20));
        String base = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        int x = 5;
        for (int i = 0; i < 4; ++i) {
            int degree = new Random().nextInt() % 30;
            String ch = base.charAt(new Random().nextInt(base.length())) + "";
            sb.append(ch);
            g.rotate(degree * Math.PI / 180, x, 20);
            g.drawString(ch, x, 20);
            g.rotate(-degree * Math.PI / 180, x, 20);
            x += 30;
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}
