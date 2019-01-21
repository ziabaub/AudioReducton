/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioreduction;

public class FFT {

    private static final Complex ZERO = new Complex(0, 0);

    // Do not instantiate.
    public FFT() { }

    /**
     * Returns the FFT of the specified complex array.
     *
     * @param  x the complex array
     * @return the FFT of the complex array {@code x}
     * @throws IllegalArgumentException if the length of {@code x} is not a power of 2
     */
    public  Complex[] fft(Complex[] x) {
        int n = x.length;

        // base case
        if (n == 1) {
            return new Complex[] { x[0] };
        }

        // radix 2 Cooley-Tukey FFT
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }

        // fft of even terms
        Complex[] even = new Complex[n/2];
        for (int k = 0; k < n/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd  = even;  // reuse the array
        for (int k = 0; k < n/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[n];
        for (int k = 0; k < n/2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + n/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }
}