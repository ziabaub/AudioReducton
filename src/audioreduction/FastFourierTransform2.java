
package audioreduction;

//import static java.lang.Math.*;
// 
public class FastFourierTransform2 {
// 
//    public static int bitReverse(int n, int bits) {
//        int reversedN = n;
//        int count = bits - 1;
// 
//        n >>= 1;
//        while (n > 0) {
//            reversedN = (reversedN << 1) | (n & 1);
//            count--;
//            n >>= 1;
//        }
// 
//        return ((reversedN << count) & ((1 << bits) - 1));
//    }
// 
//    public void fft(Complex[] buffer) {
// 
//        int bits = (int) (log(buffer.length) / log(2));
//        for (int j = 1; j < buffer.length / 2; j++) {
// 
//            int swapPos = bitReverse(j, bits);
//            Complex temp = buffer[j];
//            buffer[j] = buffer[swapPos];
//            buffer[swapPos] = temp;
//        }
// 
//        for (int N = 2; N <= buffer.length; N <<= 1) {
//            for (int i = 0; i < buffer.length; i += N) {
//                for (int k = 0; k < N / 2; k++) {
// 
//                    int evenIndex = i + k;
//                    int oddIndex = i + k + (N / 2);
//                    Complex even = buffer[evenIndex];
//                    Complex odd = buffer[oddIndex];
// 
//                    double term = (-2 * PI * k) / (double) N;
//                    Complex exp = (new Complex(cos(term), sin(term)).mult(odd));
// 
//                    buffer[evenIndex] = even.add(exp);
//                    buffer[oddIndex] = even.sub(exp);
//                }
//            }
//        }
//    }
//    public static double[] customFFT(double[] data) {
//
//    // The method is renamed from FFT
//    double[] fft = new double[data.length]; // this is where we will store the output (fft)
//    Complex[] fftComplex = new Complex[data.length]; // the FFT function requires complex format
//    for (int i = 0; i < data.length; i++) {
//      fftComplex[i] = new Complex(data[i], 0.0); // make it complex format (imaginary = 0)
//    }
//    FFT(fftComplex, FourierTransform.Direction.Forward);
//    for (int i = 0; i < data.length; i++) {
//      fft[i] = fftComplex[i].magnitude(); // back to double
//      //fft[i] = Math.Log10(fft[i]); // convert to dB
//    }
//    return fft;
//    //todo: this could be much faster by reusing variables
//  }
}
