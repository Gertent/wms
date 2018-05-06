package com.rmd.wms.web.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;


public class PdfGenerate {

    private static Logger logger= Logger.getLogger(PdfGenerate.class);

    protected static PropertiesLoader propertiesLoader =
            new PropertiesLoader("classpath:/application.properties");
    private static final Map versionMap = new HashMap();

    static {
        versionMap.put("1.2", new Character(PdfWriter.VERSION_1_2));
        versionMap.put("1.3", new Character(PdfWriter.VERSION_1_3));
        versionMap.put("1.4", new Character(PdfWriter.VERSION_1_4));
        versionMap.put("1.5", new Character(PdfWriter.VERSION_1_5));
        versionMap.put("1.6", new Character(PdfWriter.VERSION_1_6));
        versionMap.put("1.7", new Character(PdfWriter.VERSION_1_7));
    }
    /**
     * Renders the XML file at the given URL as a PDF file
     * at the target location.
     *
     * @param url url for the XML file to render
     * @param pdf path to the PDF file to create
     * @throws java.io.IOException       if the URL or PDF location is
     *                           invalid
     * @throws com.itextpdf.text.DocumentException if an error occurred
     *                           while building the Document.
     */
    public static void renderToPDF(String url, String pdf)
            throws IOException, DocumentException {

        renderToPDF(url, pdf, null);
    }
    /**
     * Renders the XML file at the given URL as a PDF file
     * at the target location.
     *
     * @param url url for the XML file to render
     * @param pdf path to the PDF file to create
     * @param pdfVersion version of PDF to output; null uses default version
     * @throws java.io.IOException       if the URL or PDF location is
     *                           invalid
     * @throws com.itextpdf.text.DocumentException if an error occurred
     *                           while building the Document.
     */
    public static void renderToPDF(String url, String pdf, Character pdfVersion)
            throws IOException, DocumentException {

        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();
        try {
			fontResolver.addFont(propertiesLoader.getProperty("pdf.template.font"), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		} catch (com.lowagie.text.DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        renderer.setDocument(url);
        if (pdfVersion != null) renderer.setPDFVersion(pdfVersion.charValue());
        doRenderToPDF(renderer, pdf);
    }

    /**
     * Renders the XML file as a PDF file at the target location.
     *
     * @param file XML file to render
     * @param pdf  path to the PDF file to create
     * @throws java.io.IOException       if the file or PDF location is
     *                           invalid
     * @throws com.itextpdf.text.DocumentException if an error occurred
     *                           while building the Document.
     */
    public static void renderToPDF(File file, String pdf)
            throws IOException, DocumentException {

        renderToPDF(file, pdf, null);
    }

    /**
     * Renders the XML file as a PDF file at the target location.
     *
     * @param file XML file to render
     * @param pdf  path to the PDF file to create
     * @param pdfVersion version of PDF to output; null uses default version
     * @throws java.io.IOException       if the file or PDF location is
     *                           invalid
     * @throws com.itextpdf.text.DocumentException if an error occurred
     *                           while building the Document.
     */
    public static void renderToPDF(File file, String pdf, Character pdfVersion)
            throws IOException, DocumentException {

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(file);
        ITextFontResolver fontResolver = renderer.getFontResolver();
        try {
			fontResolver.addFont(propertiesLoader.getProperty("pdf.template.font"), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		} catch (com.lowagie.text.DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (pdfVersion != null) renderer.setPDFVersion(pdfVersion.charValue());
        doRenderToPDF(renderer, pdf);
    }

    /**
     * Internal use, runs the render process
     * @param renderer
     * @param pdf
     * @throws com.itextpdf.text.DocumentException
     * @throws java.io.IOException
     */
    private static void doRenderToPDF(ITextRenderer renderer, String pdf)
            throws IOException, DocumentException {
        OutputStream os = null;
        try {
            os = new FileOutputStream(pdf);
            renderer.layout();
            try {
				renderer.createPDF(os);
			} catch (com.lowagie.text.DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            os.close();
            os = null;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * Renders a file or URL to a PDF. Command line use: first
     * argument is URL or file path, second
     * argument is path to PDF file to generate.
     *
     * @param args see desc
     * @throws java.io.IOException if source could not be read, or if
     * PDF path is invalid
     * @throws com.itextpdf.text.DocumentException if an error occurs while building
     * the document
     */
    public static void main(String[] args) throws IOException, DocumentException {
        try {
            new PdfGenerate().htmlToPdf4();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void generatePdf(String[] args) throws IOException, DocumentException {
        if (args.length < 2) {
            usage("Incorrect argument list.");
        }
        Character pdfVersion = null;
        if (args.length == 3) {
            pdfVersion = checkVersion(args[2]);
        }
        String url = args[0];
        if (url.indexOf("://") == -1) {
            // maybe it's a file
            File f = new File(url);
            if (f.exists()) {
                PdfGenerate.renderToPDF(f, args[1], pdfVersion);
            } else {
                usage("File to render is not found: " + url);
            }
        } else {
            PdfGenerate.renderToPDF(url, args[1], pdfVersion);
        }
    }

    private static Character checkVersion(String version) {
        final Character val = (Character) versionMap.get(version.trim());
        if (val == null) {
            usage("Invalid PDF version number; use 1.2 through 1.7");
        }
        return val;
    }

    /** prints out usage information, with optional error message
     * @param err
     */
    private static void usage(String err) {
        if (err != null && err.length() > 0) {
            System.err.println("==>" + err);
        }
        System.err.println("Usage: ... url pdf [version]");
        System.err.println("   where version (optional) is between 1.2 and 1.7");
        System.exit(1);
    }

    public  void htmlToPdf3() throws Exception{

        String inputFile ="F:/project_code/rmd_wms_parent/rmd_wms_web/src/main/webapp/views/outstock/testPdf1.html";
        String outFile ="d:/tmp/test66.pdf";

        OutputStream os = null;
        os = new FileOutputStream(outFile);

        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont("C:/Windows/fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        String url = new File( inputFile ).toURI().toURL().toString();
        logger.info("=============url:"+url);
        renderer.setDocument(url);//url
        renderer.layout();
        renderer.createPDF(os);
        logger.info("======转换成功!");
        os.close();

    }
    
    public  void htmlToPdf4() throws Exception{

        String outFile ="d:/tmp/test66.pdf";
        StringBuffer sBuffer=new StringBuffer();
        try
        {
           // g该url返回一个jsp界面
           URL url = new URL("http://192.168.0.86:28081/rmd_wms_web/printpdf/testPdf6666");
           // 内容是文本，直接以缓冲字符流读取
           BufferedReader reader = new BufferedReader(new InputStreamReader(
                 url.openStream(), "UTF-8"));
           String data = null;
           
           while ((data = reader.readLine()) != null){
        	   sBuffer.append(data);             
           }
           logger.info(sBuffer.toString());
           reader.close();
        }
        catch (IOException e){
           e.printStackTrace();
        }

        OutputStream os = null;
        os = new FileOutputStream(outFile);

        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont("C:/Windows/fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//        String url = new File( inputFile ).toURI().toURL().toString();
//        logger.info("=============url:"+url);
        renderer.setDocumentFromString(sBuffer.toString());
        renderer.layout();
        renderer.createPDF(os);
        logger.info("======转换成功!");
        os.close();

    }


    public  void htmlToPdf2() throws Exception {

        String outputFile ="d:/tmp/demo_3.pdf";

        OutputStream os = new FileOutputStream(outputFile);

        ITextRenderer renderer = new ITextRenderer();

        ITextFontResolver fontResolver = renderer.getFontResolver();

        fontResolver.addFont("C:/Windows/fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        StringBuffer html = new StringBuffer();

        // DOCTYPE 必需写否则类似于 这样的字符解析会出现错误

        html.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");

                html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">").
        append("<head>")
                .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>")
        .append("<style type=\"text/css\" mce_bogus=\"1\">body {font-family: SimSun;}</style>")
                .append("</head>")
                .append("<body>");
        html.append("<div>支持中文！</div>");
        html.append("</body></html>");
        renderer.setDocumentFromString(html.toString());
        // 解决图片的相对路径问题
        // renderer.getSharedContext().setBaseURL("file:/F:/teste/html/");
        renderer.layout();
        renderer.createPDF(os);
        logger.info("======转换成功!");
        os.close();
    }
   /* public  void test() throws Exception {
        String inputFile = "conf/template/test.html";
        String url = new File(inputFile).toURI().toURL().toString();
        String outputFile = "firstdoc.pdf";
        OutputStream os = new FileOutputStream(outputFile);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(url);

        // 解决中文支持问题
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont("C:/Windows/Fonts/arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        // 解决图片的相对路径问题
        renderer.getSharedContext().setBaseURL("file:/D:/Work/Demo2do/Yoda/branch/Yoda%20-%20All/conf/template/");

        renderer.layout();
        renderer.createPDF(os);

        os.close();
    }*/
    private static String create(String url, String pdfUrl, String pdfName) throws IOException {
		OutputStream os = null;
		try {
			ITextRenderer renderer = new ITextRenderer();
			 renderer.setDocumentFromString(url);
//			URL ur = new URL(url);
//			renderer.setDocument(url);
//			Document doc =null;
//			renderer.setDocument(doc, url);

			// ????????
			ITextFontResolver fontResolver = renderer.getFontResolver();
			String paths = "C:/Windows/fonts/simsun.ttc";
			fontResolver.addFont(paths, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

			renderer.layout();
			os = new FileOutputStream(pdfUrl);
			renderer.createPDF(os);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null)
				os.close();
		}
		// ????
		// addWater(pdfUrl2, pdfUrl, path);
		return "success";
	}
   
}
