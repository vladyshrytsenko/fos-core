package com.example.foscore.service;

import com.example.foscore.controller.feign.UserServiceClient;
import com.example.foscore.model.dto.UserDto;
import com.example.foscore.model.entity.Order;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.String.*;

@Service
@RequiredArgsConstructor
public class ReportService {

    @Async("taskExecutor")
    public CompletableFuture<byte[]> makeReport() {
        List<UserDto> users = this.userServiceClient.getAllUsers();

        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            this.generatePdf(users, document);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CompletableFuture.completedFuture( outputStream.toByteArray() );
    }

    private void generatePdf(List<UserDto> users, Document document) throws DocumentException {
        Paragraph preface = new Paragraph();

        preface.add(new Paragraph("Report: Food ordering system", catFont));
        document.add(Chunk.NEWLINE);
        document.add(preface);

        PdfPTable table = this.createTable(users);
        document.add(table);
    }

    private PdfPTable createTable(List<UserDto> users) {
        PdfPTable table = new PdfPTable(4);

        PdfPCell usernameCell = new PdfPCell(new Phrase("username"));
        usernameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(usernameCell);

        PdfPCell ordersPerMonthCell = new PdfPCell(new Phrase("orders/month"));
        ordersPerMonthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(ordersPerMonthCell);

        PdfPCell avgCostPerMonth = new PdfPCell(new Phrase("avg cost/month"));
        avgCostPerMonth.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(avgCostPerMonth);

        PdfPCell percentOfAllOrders = new PdfPCell(new Phrase("% of all orders"));
        percentOfAllOrders.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(percentOfAllOrders);

        table.setHeaderRows(1);

        try (ExecutorService executorService = Executors.newFixedThreadPool(5)) {
            List<Future<List<String>>> futures = new ArrayList<>();

            long countForCurrentMonth = this.orderService.getOrdersCountForCurrentMonth();
            for (UserDto user : users) {
                futures.add(
                    executorService.submit(() -> this.processUserOrders(user, countForCurrentMonth))
                );
            }

            futures.forEach(future -> {
                try {
                    List<String> row = future.get();
                    row.forEach(table::addCell);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            executorService.shutdown();
        }
        return table;
    }

    @Transactional(readOnly = true)
    private List<String> processUserOrders(UserDto user, long ordersCountForCurrentMonth) {
        Long userId = user.getId();
        String username = user.getUsername();

        List<Order> ordersByUserId = this.orderService.getOrdersForCurrentMonth(userId);

        double ordersCost = ordersByUserId.stream()
            .mapToDouble(Order::getTotalPrice)
            .sum();

        float ordersByUserIdSize = ordersByUserId.size();
        double avgCost = ordersByUserIdSize == 0 ? 0 : (ordersCost / ordersByUserIdSize);

        return List.of(
            username,
            String.valueOf(ordersByUserIdSize),
            format("%.2f", avgCost),
            String.valueOf((ordersByUserIdSize / ordersCountForCurrentMonth) * 100).concat("%")
        );
    }

    private final UserServiceClient userServiceClient;
    private final OrderService orderService;

    private static final Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
}
