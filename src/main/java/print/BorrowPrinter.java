package print;

import bean.TbBorrowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;


public class BorrowPrinter implements Printable {

    private static Logger LOG = LoggerFactory.getLogger(BorrowPrinter.class);

    private TbBorrowRecord record;

    private BorrowPrinter(){}

    public BorrowPrinter(TbBorrowRecord record){
        this.record = record;
    }


    public void printBorrow(){

        int height = 175 + 3 * 15 + 20;

        // 通俗理解就是书、文档
        Book book = new Book();

        // 打印格式
        PageFormat pf = new PageFormat();
        pf.setOrientation(PageFormat.PORTRAIT);

        // 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
        Paper p = new Paper();
        p.setSize(230,  height + 200);
        p.setImageableArea(5, -20, 230, height + 200);
        pf.setPaper(p);

        // 把 PageFormat 和 Printable 添加到书中，组成一个页面
        book.append(this, pf);

        // 获取打印服务对象
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(book);
        try {
            job.print();
        } catch (PrinterException e) {
            LOG.error("打印任务错误:",e);
        }
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page)  {

        if (page > 0) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Default", Font.PLAIN, 12));

        g2d.drawString("借用清单", 50, 30);
        g2d.drawString("-------------------------------", 13, 40);

        g2d.drawString("名称："+record.getDeviceName(), 13, 60);
        g2d.drawString("型号："+record.getDeviceType(), 13, 80);
        g2d.drawString("数量："+record.getBorrowNum(), 13, 100);
        g2d.drawString("借用人："+record.getBorrowUserName(), 13, 120);
        g2d.drawString("借用日期："+new SimpleDateFormat("yyyy-MM-dd").format(record.getBorrowDate()), 13, 140);
        g2d.drawString("借出保管员："+record.getBorrowClerkUserName(), 13, 160);

        g2d.drawString("-------------------------------", 13, 180);

        return PAGE_EXISTS;
    }

}