package application;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class EdgeExtraction {
    private BufferedImage Gx, Gy;

    // Metoda pentru detectarea marginilor
    public BufferedImage detect(BufferedImage img) {
        // Matricile pentru detectia marginilor folosind operatorul Sobel
        float[] x1 = {-1, 0, 1, -2, 0, 2, -1, 0, 1};
        float[] y1 = {-1, -2, -1, 0, 0, 0, 1, 2, 1};

        // Crearea kernel-urilor pentru convolutie
        Kernel MatrixA = new Kernel(3, 3, x1);
        Kernel MatrixB = new Kernel(3, 3, y1);

        // Operatiile de convolutie pentru obtinerea gradientului pe directiile X si Y
        ConvolveOp convolve1 = new ConvolveOp(MatrixA);
        ConvolveOp convolve2 = new ConvolveOp(MatrixB);

        this.Gx = convolve1.filter(img, null);
        this.Gy = convolve2.filter(img, null);

        // Procesarea fiecarui pixel pentru a determina marginea
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                double result = G(i, j);

                // Setarea pixelului alb sau negru in functie de pragul rezultatului
                if (result < 20726564.99) {
                    img.setRGB(i, j, Color.white.getRGB());
                } else {
                    img.setRGB(i, j, Color.black.getRGB());
                }
            }
        }
        return img;
    }

    // Metoda pentru conversia imaginii in tonuri de gri
    public BufferedImage greyscale(BufferedImage img) {
        // Numarul maxim de culori
        double max = 23777215;

        // Matricile pentru detectia marginilor folosind operatorul Sobel
        float[] x1 = {-1, 0, 1, -2, 0, 2, -1, 0, 1};
        float[] y1 = {-1, -2, -1, 0, 0, 0, 1, 2, 1};

        // Crearea kernel-urilor pentru convolutie
        Kernel MatrixA = new Kernel(3, 3, x1);
        Kernel MatrixB = new Kernel(3, 3, y1);

        // Operatiile de convolutie pentru obtinerea gradientului pe directiile X si Y
        ConvolveOp convolve1 = new ConvolveOp(MatrixA);
        ConvolveOp convolve2 = new ConvolveOp(MatrixB);

        this.Gx = convolve1.filter(img, null);
        this.Gy = convolve2.filter(img, null);

        // Procesarea fiecarui pixel pentru a determina valoarea in tonuri de gri
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                double result1 = G(i, j);
                // Calcularea valorii in tonuri de gri
                float greyscaleValue = (float) (result1 / max);
                greyscaleValue = 1 - greyscaleValue;
                float red = 255 * greyscaleValue;
                float blue = 255 * greyscaleValue;
                float green = 255 * greyscaleValue;
                Color gray2 = new Color((int) red, (int) green, (int) blue);
                img.setRGB(i, j, gray2.getRGB());
            }
        }
        return img;
    }

    // Metoda pentru calcularea gradientului total la un pixel (magnitudinea gradientului)
    private double G(int x, int y) {
        // Valoarea minima trebuie sa fie 0, iar cea maxima 16777215
        int derp = this.Gx.getRGB(x, y);
        int herp = this.Gy.getRGB(x, y);

        // Calcularea rezultatului folosind teorema lui Pitagora
        double result1 = Math.sqrt(Math.pow(derp, 2.0) + Math.pow(herp, 2.0));
        return result1;
    }
}
