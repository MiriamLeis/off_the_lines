package es.ucm.gdv.logic;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

enum Mode{
    EASY,
    HARD
}

public class Utils {
    static public int _WIDTH = 640;
    static public int _HEIGHT = 480;

    //Rotamos una linea
    static public double[][] rotate(Line line, double angle) {

        double[] midpoint = line.getPosition();
        double[] a = new double[]{line.getAVertex()[0], line.getAVertex()[1]};
        double[] b = new double[]{line.getBVertex()[0], line.getBVertex()[1]};

        double[] a_mid = new double[]{a[0] - midpoint[0], a[1] - midpoint[1]};

        double[] b_mid =new double[]{b[0] - midpoint[0], b[1] - midpoint[1]};

        angle = Math.toRadians(angle);


        a[0] = Math.cos(angle) * a_mid[0] - Math.sin(angle) * a_mid[1];
        a[1] = Math.sin(angle) * a_mid[0] + Math.cos(angle) * a_mid[1];
        b[0] = Math.cos(angle) * b_mid[0] - Math.sin(angle) * b_mid[1];
        b[1] = Math.sin(angle) * b_mid[0] + Math.cos(angle) * b_mid[1];



        a[0] = a[0] + midpoint[0];
        a[1] = a[1] + midpoint[1];
        b[0] = b[0] + midpoint[0];
        b[1] = b[1] + midpoint[1];


        return new double[][] {{a[0], a[1]}, {b[0], b[1]}};
    }

    //Vector perpendicular a otro
    static public float[] getPerpendicular(boolean dir, float[] vector){
        float[] perpendicular = new float[2];
        if(dir){
            perpendicular[0] = - vector[1];
            perpendicular[1] = vector[0];
        }
        else{
            perpendicular[0] = vector[1];
            perpendicular[1] = - vector[0];
        }

        return  perpendicular;
    }

    // Recibe dos segmentos y devuelve el punto donde se cruzan (si lo hacen).
    static public double[] segmentsIntersection(double x1, double y1, double x2, double y2,
                                               double x3, double y3, double x4, double y4, boolean segmentInSegment) {

        double[] seg1 = new double[]{x2 - x1, y2 - y1};
        double[] seg2 = new double[]{x3 - x1, y3 - y1};
        double[] seg3 = new double[]{x4 - x1, y4 - y1};

        double pVectorial1 = (seg1[0] * seg2[1]) - (seg1[1] * seg2[0]);
        double pVectorial2 = (seg1[0] * seg3[1]) - (seg1[1] * seg3[0]);

        if (!((pVectorial1 > 0 && pVectorial2 > 0) || (pVectorial1 < 0 && pVectorial2 < 0))) {
            double[] E = new double[]{x2-x1, y2-y1 };
            double[] F = new double[]{x4-x3, y4-y3};
            double[] P = new double[]{-E[1], E[0]};
            double h = ( (x1-x3)*P[0] + (y1-y3)*P[1] ) / ( F[0] * P[0] + F[1] * P[1]);

            double[] E2 = new double[]{x4-x3, y4-y3 };
            double[] F2 = new double[]{x2-x1, y2-y1};
            double[] P2 = new double[]{-E2[1], E2[0]};
            double h2 = ( (x3-x1)*P2[0] + (y3-y1)*P2[1] ) / ( F2[0] * P2[0] + F2[1] * P2[1]);

            if(segmentInSegment) {
                if (h >= 0.0 && h <= 1.0 && h2 >= 0.0 && h2 <= 1.0) {
                    double x = x3 + F[0] * h;
                    double y = y3 + F[1] * h;

                    return new double[]{x3 + F[0] * h, y3 + F[1] * h};

                } else
                    return null;
            }
            else{
                if (h > 0.0 && h < 1.0 && h2 > 0.0 && h2 < 1.0) {
                    double x = x3 + F[0] * h;
                    double y = y3 + F[1] * h;

                    return new double[]{x3 + F[0] * h, y3 + F[1] * h};

                } else
                    return null;
            }

        }

        return null;
    }


    // Recibe un segmento y un punto y devuelve el cuadrado de la distancia del punto al segmento.
    // Tened en cuenta que esto no es lo mismo que la distancia del punto a la recta definida
    // por el segmento.
    static public double sqrDistancePointSegment(double x1, double y1,
                                                double x2, double y2,
                                                double x, double y) {
        if(isBetween(x1, y1, x2, y2, x, y))
            return 0;

        double A = x - x1;
        double B = y - y1;
        double C = x2 - x1;
        double D = y2 - y1;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = -1;
        if (len_sq != 0)
            param = dot / len_sq;

        double xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        }
        else if (param > 1) {
            xx = x2;
            yy = y2;
        }
        else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        double dx = x - xx;
        double dy = y - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }

    static private boolean isBetween(double x1, double y1, double x2, double y2, double x3, double y3) {
        return min(x1, x2) <= x3 && x3 <= max(x1, x2) &&
                min(y1, y2) <= y3 && y3 <= max(y1, y2);
    }
}