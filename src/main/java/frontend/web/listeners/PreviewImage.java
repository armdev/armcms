package frontend.web.listeners;

//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
//import java.awt.Graphics2D;
//import java.awt.Image;
//import java.awt.RenderingHints;
//import java.awt.image.BufferedImage;
//import javax.imageio.ImageIO;
import backend.entities.FileEntity;
import backend.facades.FileManagerFacade;
import com.mongodb.gridfs.GridFSDBFile;
import java.io.*;
import javax.faces.context.FacesContext;


import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Armen Arzumanyan
 */
//@WebServlet(urlPatterns = {
//    "/PreviewImage"
//})
public class PreviewImage extends HttpServlet implements SingleThreadModel {

    private static final long serialVersionUID = -6624464650990859671L;
    // @ManagedProperty("#{FileManagerFacade}")    
    private FileManagerFacade fileAction = new FileManagerFacade();

    @Override
    public void init() throws ServletException {
        super.init();


    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPreviewImage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    private void doPreviewImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        String fileIdStr = request.getParameter("fileId");
        String widthStr = request.getParameter("w");

        GridFSDBFile file = null;
        int width = 0;
        if (widthStr != null && widthStr.length() > 0) {
            try {
                width = Integer.parseInt(widthStr);
            } catch (NumberFormatException e) {
            }
        }
        if (fileIdStr != null) {

            if (fileAction != null) {
                file = fileAction.getFile(fileIdStr.trim());
            }
            if (file != null) {
                byte[] content = IOUtils.toByteArray(file.getInputStream());
                if (content != null) {
                    String mimeType = file.getContentType();

                    response.addHeader("Pragma", "cache");
                    response.addHeader("Cache-Control", "max-age=3600, must-revalidate");
                    response.addDateHeader("Expires", System.currentTimeMillis() + 1000 * 3600 * 10);

                    response.setContentType(mimeType);

                    try {
                        if (mimeType != null
                                && (mimeType.equalsIgnoreCase("image/gif")
                                || mimeType.equalsIgnoreCase("image/x-png")
                                || mimeType.equalsIgnoreCase("image/png")
                                || mimeType.equalsIgnoreCase("image/jpg")
                                || mimeType.equalsIgnoreCase("image/jpeg"))
                                || width == 0) {
                            response.getOutputStream().write(content);
                        } else {
//                            ByteArrayInputStream bi = new ByteArrayInputStream(content);
//                            InputStream thumbStream = scaleImageJPG(bi, width);
//                            byte[] thumbContent = new byte[thumbStream.available()];
//                            thumbStream.read(thumbContent);
                            response.getOutputStream().write(content);
                        }
                    } catch (IOException e) {
                        //log.error("file content send error");
                        e.printStackTrace();
                    } catch (Exception e) {
                        //log.error("file exception: " + e);
                        e.printStackTrace();
                    } finally {
                        content = null;
                        file = null;
                    }
                    return;
                }
            } else {
                //TODO add page not found
                response.addHeader("Pragma", "no-cache");
                response.addDateHeader("Expires", System.currentTimeMillis() - 1000 * 3600);
                try {
                    response.getWriter().println("file object is null");
                } catch (Exception e) {
                }
                return;
            }
        }
        //TODO add page not found
        response.addHeader("Pragma", "no-cache");
        response.addDateHeader("Expires", System.currentTimeMillis() - 1000 * 3600);

        try {
            response.getWriter().println("file id is not set");
        } catch (Exception e) {
        }
        //log.debug("file ID parameter is not set or file is not found");
        return;
    }
    // </editor-fold>

//    public static InputStream scaleImageJPG(InputStream p_image, int p_width) throws Exception {
//
//        InputStream imageStream = new BufferedInputStream(p_image);
//        Image image = (Image) ImageIO.read(imageStream);
//
//        int thumbWidth = p_width;
//        int thumbHeight = 0;
//
//        int imageWidth = image.getWidth(null);
//        int imageHeight = image.getHeight(null);
//        double imageRatio = (double) imageWidth / (double) imageHeight;
//        thumbHeight = (int) (thumbWidth / imageRatio);
//
//        // Draw the scaled image
//        BufferedImage thumbImage = new BufferedImage(thumbWidth,
//                thumbHeight, BufferedImage.TYPE_INT_RGB);
//        Graphics2D graphics2D = thumbImage.createGraphics();
//        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
//
//        // Write the scaled image to the outputstream
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
//        int quality = 100; // Use between 1 and 100, with 100 being highest quality
//
//        quality = Math.max(0, Math.min(quality, 100));
//        param.setQuality((float) quality / 100.0f, false);
//        encoder.setJPEGEncodeParam(param);
//        encoder.encode(thumbImage);
//        ImageIO.write(thumbImage, "IMAGE_JPG", out);
//
//        // Read the outputstream into the inputstream for the return value
//        ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
//
//        return bis;
//    }

    public void setFileAction(FileManagerFacade fileAction) {
        this.fileAction = fileAction;
    }
}