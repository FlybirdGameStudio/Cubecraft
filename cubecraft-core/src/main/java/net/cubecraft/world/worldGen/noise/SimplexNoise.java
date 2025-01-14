package net.cubecraft.world.worldGen.noise;

import java.util.Random;

public class SimplexNoise extends Noise{
    protected static final int[][] GRADIENT = new int[][]{{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}, {1, 1, 0}, {0, -1, 1}, {-1, 1, 0}, {0, -1, -1}};
    private static final double SQRT_3 = Math.sqrt(3.0);
    private static final double F2 = 0.5 * (SQRT_3 - 1.0);
    private static final double G2 = (3.0 - SQRT_3) / 6.0;
    private final int[] p = new int[512];
    public final double xo;
    public final double yo;
    public final double zo;

    public SimplexNoise(Random randomSource) {
        int i;
        this.xo = randomSource.nextDouble() * 256.0;
        this.yo = randomSource.nextDouble() * 256.0;
        this.zo = randomSource.nextDouble() * 256.0;
        for (i = 0; i < 256; ++i) {
            this.p[i] = i;
        }
        for (i = 0; i < 256; ++i) {
            int j = randomSource.nextInt(256 - i);
            int k = this.p[i];
            this.p[i] = this.p[j + i];
            this.p[j + i] = k;
        }
    }

    private int p(int i) {
        return this.p[i & 0xFF];
    }

    protected static double dot(int[] is, double d, double e, double f) {
        return (double)is[0] * d + (double)is[1] * e + (double)is[2] * f;
    }

    private double getCornerNoise3D(int i, double d, double e, double f, double g) {
        double j;
        double h = g - d * d - e * e - f * f;
        if (h < 0.0) {
            j = 0.0;
        } else {
            h *= h;
            j = h * h * SimplexNoise.dot(GRADIENT[i], d, e, f);
        }
        return j;
    }

    @Override
    public double getValue(double xx, double yy) {
        int o;
        int n;
        double k;
        double m;
        int j;
        double g;
        double f = (xx + yy) * F2;
        int i = (int) Math.floor(xx + f);
        double h = (double)i - (g = (double)(i + (j = (int) Math.floor(yy + f))) * G2);
        double l = xx - h;
        if (l > (m = yy - (k = (double)j - g))) {
            n = 1;
            o = 0;
        } else {
            n = 0;
            o = 1;
        }
        double p = l - (double)n + G2;
        double q = m - (double)o + G2;
        double r = l - 1.0 + 2.0 * G2;
        double s = m - 1.0 + 2.0 * G2;
        int t = i & 0xFF;
        int u = j & 0xFF;
        int v = this.p(t + this.p(u)) % 12;
        int w = this.p(t + n + this.p(u + o)) % 12;
        int x = this.p(t + 1 + this.p(u + 1)) % 12;
        double y = this.getCornerNoise3D(v, l, m, 0.0, 0.5);
        double z = this.getCornerNoise3D(w, p, q, 0.0, 0.5);
        double aa = this.getCornerNoise3D(x, r, s, 0.0, 0.5);
        return 70.0 * (y + z + aa);
    }

    public double getValue(double d, double e, double f) {
        int y;
        int x;
        int w;
        int v;
        int u;
        int t;
        double g = 0.3333333333333333;
        double h = (d + e + f) * 0.3333333333333333;
        int i = (int) Math.floor(d + h);
        int j = (int) Math.floor(e + h);
        int k = (int) Math.floor(f + h);
        double l = 0.16666666666666666;
        double m = (double)(i + j + k) * 0.16666666666666666;
        double n = (double)i - m;
        double o = (double)j - m;
        double p = (double)k - m;
        double q = d - n;
        double r = e - o;
        double s = f - p;
        if (q >= r) {
            if (r >= s) {
                t = 1;
                u = 0;
                v = 0;
                w = 1;
                x = 1;
                y = 0;
            } else if (q >= s) {
                t = 1;
                u = 0;
                v = 0;
                w = 1;
                x = 0;
                y = 1;
            } else {
                t = 0;
                u = 0;
                v = 1;
                w = 1;
                x = 0;
                y = 1;
            }
        } else if (r < s) {
            t = 0;
            u = 0;
            v = 1;
            w = 0;
            x = 1;
            y = 1;
        } else if (q < s) {
            t = 0;
            u = 1;
            v = 0;
            w = 0;
            x = 1;
            y = 1;
        } else {
            t = 0;
            u = 1;
            v = 0;
            w = 1;
            x = 1;
            y = 0;
        }
        double z = q - (double)t + 0.16666666666666666;
        double aa = r - (double)u + 0.16666666666666666;
        double ab = s - (double)v + 0.16666666666666666;
        double ac = q - (double)w + 0.3333333333333333;
        double ad = r - (double)x + 0.3333333333333333;
        double ae = s - (double)y + 0.3333333333333333;
        double af = q - 1.0 + 0.5;
        double ag = r - 1.0 + 0.5;
        double ah = s - 1.0 + 0.5;
        int ai = i & 0xFF;
        int aj = j & 0xFF;
        int ak = k & 0xFF;
        int al = this.p(ai + this.p(aj + this.p(ak))) % 12;
        int am = this.p(ai + t + this.p(aj + u + this.p(ak + v))) % 12;
        int an = this.p(ai + w + this.p(aj + x + this.p(ak + y))) % 12;
        int ao = this.p(ai + 1 + this.p(aj + 1 + this.p(ak + 1))) % 12;
        double ap = this.getCornerNoise3D(al, q, r, s, 0.6);
        double aq = this.getCornerNoise3D(am, z, aa, ab, 0.6);
        double ar = this.getCornerNoise3D(an, ac, ad, ae, 0.6);
        double as = this.getCornerNoise3D(ao, af, ag, ah, 0.6);
        return 32.0 * (ap + aq + ar + as);
    }
}