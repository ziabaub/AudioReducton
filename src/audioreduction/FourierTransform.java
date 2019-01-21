
package audioreduction;


public class FourierTransform {  
//  public enum Direction {
//    Forward (1),
//    Backward(-1);
//    private final int value;
//    
//    Direction(int value) {
//      this.value = value;
//    }
//    public int getValue() {
//      return this.value;
//    }
//  } 
//  
//  // One dimensional Discrete Fourier Transform.
//  public static void DFT(Complex[] data, Direction direction) {
//      int n = data.length;
//      double arg, cos, sin;
//      Complex[] dst = new Complex[n];
//      
//      for (int i = 0; i < dst.length; i++) {
//        dst[i] = Complex.ZERO;
//
//        arg = -(int)direction.getValue() * 2.0 * Math.PI * (double)i / (double)n;
//
//        // sum source elements
//        for (int j = 0; j < data.length; j++) {
//          cos = Math.cos(j * arg);
//          sin = Math.sin(j * arg);
//
//          double re = data[j].real() * cos - data[j].imag() * sin;
//          double im = data[j].real() * sin + data[j].imag() * cos;
//
//          dst[i] = Complex.add(dst[i], new Complex(re, im));//dst[i] += new Complex(re, im);
//        }
//      }
//      
//      // copy elements
//      if (direction == Direction.Forward) {
//        // devide also for forward transform
//        for (int i = 0; i < data.length; i++) {
//          data[i] = Complex.div(data[i], Double.valueOf(n));// data[i] /= n;
//        }
//      } else {
//        System.arraycopy(dst, 0, data, 0, data.length);
//      }
//  }
//  
//  // Two dimensional Discrete Fourier Transform.
//  public static void DFT2(Complex[][] data, Direction direction) {
//    int n = data.length;	// rows
//    int m = data[0].length;	// columns
//    double arg, cos, sin;
//    Complex[] dst = new Complex[Math.max(n, m)];
//
//    // process rows
//    for (int i = 0; i < n; i++) {
//      for (int j = 0; j < dst.length; j++) {
//        dst[j] = Complex.ZERO;
//        arg = -(int)direction.value * 2.0 * Math.PI * (double)j / (double)m;
//
//        // sum source elements
//        for (int k = 0; k < m; k++) {
//          cos = Math.cos(k * arg);
//          sin = Math.sin(k * arg);
//          double re = data[i][k].real() * cos - data[i][k].imag() * sin;
//          double im = data[i][k].real() * sin + data[i][k].imag() * cos;
//
//          dst[j] = Complex.add(dst[j], new Complex(re, im)); //dst[j] += new Complex(re, im);
//        }
//      }
//
//      // copy elements
//      if (direction == Direction.Forward) {
//        // devide also for forward transform
//        for (int j = 0; j < dst.length; j++) {
//          data[i][j] = Complex.div(dst[j], Double.valueOf(m)); //data[i, j] = dst[j] / m;
//        }
//      } else {
//        System.arraycopy(dst, 0, data[i], 0, dst.length); // data[i, j] = dst[j];
//      }
//    }
//    
//    // process columns
//    for (int j = 0; j < m; j++) {
//      for (int i = 0; i < n; i++) {
//        dst[i] = Complex.ZERO;
//        arg = -(int)direction.getValue() * 2.0 * Math.PI * (double)i / (double)n;
//
//        // sum source elements
//        for (int k = 0; k < n; k++) {
//          cos = Math.cos(k * arg);
//          sin = Math.sin(k * arg);
//
//          double re = data[k][j].real() * cos - data[k][j].imag() * sin;
//          double im = data[k][j].real() * sin + data[k][j].imag() * cos;
//
//          dst[i] = Complex.add(dst[i], new Complex(re, im)); //dst[i] += new Complex(re, im);
//        }
//      }
//
//      // copy elements
//      if (direction == Direction.Forward) {
//        // devide also for forward transform
//        for (int i = 0; i < dst.length; i++) {
//          data[i][j] = Complex.div(dst[i], Double.valueOf(n)); //data[i][j] = dst[i] / n;
//        }
//      } else {
//        for (int i = 0; i < dst.length; i++) {
//          data[i][j] = dst[i];
//        }
//      }
//    }
//  }
//  
//  // One dimensional Fast Fourier Transform.
//  public  void FFT(Complex[] data, Direction direction) {
//      int n = data.length;
//      int m = log2(n);
//
//      // reorder data first
//      reorderData(data);
//
//      // compute FFT
//      int tn = 1, tm;
//
//      for (int k = 1; k <= m; k++) {
//        Complex[] rotation = FourierTransform.getComplexRotation(k, direction);
//
//        tm = tn;
//        tn <<= 1;
//
//        for (int i = 0; i < tm; i++) {
//          Complex t = rotation[i];
//          
//          for (int even = i; even < n; even += tn) {
//            int odd = even + tm;
//            Complex ce = data[even];
//            Complex co = data[odd];
//
//            double tr = co.real()* t.real() - co.imag() * t.imag();
//            double ti = co.real()* t.imag() + co.imag() * t.real();
//
//            data[even] = Complex.add(data[even], new Complex(tr, ti)); //data[even] += new Complex(tr, ti);
//            data[odd] = new Complex(ce.real() - tr, ce.imag() - ti);
//          }
//        }
//      }
//
//      if (direction == Direction.Forward) {
//        for (int i = 0; i < data.length; i++) {
//          data[i] = Complex.div(data[i], Double.valueOf(n)); //data[i] /= (double)n;
//        }
//      }
//  }
//  
//  // Two dimensional Fast Fourier Transform.
//  public  void FFT2(Complex[][] data, Direction direction) {
//      int k = data.length;	// rows
//      int n = data[0].length;	// columns
//
//      // check data size
//      if (!isPowerOf2(k) || ! isPowerOf2(n))
//          throw new IllegalArgumentException("The matrix rows and columns must be a power of 2.");
//
//      if (k < MIN_LENGTH || k > MAX_LENGTH || n < MIN_LENGTH || n > MAX_LENGTH)
//          throw new IllegalArgumentException("Incorrect data length.");
//
//      // process rows
//      Complex[] row = new Complex[n];
//
//      for (int i = 0; i < k; i++) {
//        // copy row
//        System.arraycopy(data[i], 0, row, 0, row.length);
//        // transform it
//       // FourierTransform.FFT(row, direction);
//
//        // copy back
//        System.arraycopy(row, 0, data[i], 0, row.length);
//      }
//
//      // process columns
//      Complex[] col = new Complex[k];
//
//      for (int j = 0; j < n; j++) {
//        // copy column
//        for (int i = 0; i < k; i++) {
//          col[i] = data[i][j];
//        }
//
//        // transform it
//       // FourierTransform.FFT(col, direction);
//
//        // copy back
//        for (int i = 0; i < k; i++) {
//          data[i][j] = col[i];
//        }
//      }
//  }
//
//  // Get rotation of complex number
//  private static Complex[] getComplexRotation(int numberOfBits, Direction direction) {
//      int directionIndex = (direction == Direction.Forward) ? 0 : 1;
//
//      // check if the array is already calculated
//      if (COMPLEX_ROTATION[numberOfBits - 1][directionIndex] == null) {
//          int n = 1 << (numberOfBits - 1);
//          double uR = 1.0;
//          double uI = 0.0;
//          double angle = Math.PI / n * (int)direction.getValue();
//          double wR = Math.cos(angle);
//          double wI = Math.sin(angle);
//          double t;
//          Complex[] rotation = new Complex[n];
//
//          for (int i = 0; i < n; i++) {
//              rotation[i] = new Complex(uR, uI);
//              t = uR * wI + uI * wR;
//              uR = uR * wR - uI * wI;
//              uI = t;
//          }
//
//          COMPLEX_ROTATION[numberOfBits - 1][directionIndex] = rotation;
//      }
//      return COMPLEX_ROTATION[numberOfBits - 1][directionIndex];
//  }
//  
//  // Get base of binary logarithm.
//  public static int log2(int x) {
//    if (x <= 65536) {
//      if (x <= 256) {
//        if (x <= 16) {
//          if (x <= 4) {
//            if (x <= 2) {
//              if (x <= 1)
//                return 0;
//              return 1;
//            }
//            return 2;
//          }
//          if (x <= 8)
//            return 3;
//          return 4;
//        }
//        if (x <= 64) {
//          if (x <= 32)
//            return 5;
//          return 6;
//        }
//        if (x <= 128)
//          return 7;
//        return 8;
//      }
//      if (x <= 4096) {
//        if (x <= 1024) {
//          if (x <= 512)
//            return 9;
//          return 10;
//        }
//        if (x <= 2048)
//          return 11;
//        return 12;
//      }
//      if (x <= 16384) {
//        if (x <= 8192)
//          return 13;
//        return 14;
//      }
//      if (x <= 32768)
//        return 15;
//      return 16;
//    }
//
//    if (x <= 16777216) {
//      if (x <= 1048576) {
//        if (x <= 262144) {
//          if (x <= 131072)
//            return 17;
//          return 18;
//        }
//        if (x <= 524288)
//          return 19;
//        return 20;
//      }
//      if (x <= 4194304) {
//        if (x <= 2097152)
//          return 21;
//        return 22;
//      }
//      if (x <= 8388608)
//        return 23;
//      return 24;
//    }
//    if (x <= 268435456) {
//      if (x <= 67108864) {
//        if (x <= 33554432)
//          return 25;
//        return 26;
//      }
//      if (x <= 134217728)
//        return 27;
//      return 28;
//    }
//    if (x <= 1073741824) {
//      if (x <= 536870912)
//        return 29;
//      return 30;
//    }
//    return 31;
//  }
//  
//  // Calculates power of 2.
//  public static int pow2(int power) {
//    return ((power >= 0) && (power <= 30)) ? (1 << power) : 0;
//  }
//  
//  // Checks if the specified integer is power of 2.
//  public static Boolean isPowerOf2(int x) {
//    return (x > 0) ? ((x & (x - 1)) == 0) : false;
//  }
//  
//  private static final int MIN_LENGTH = 2;
//  private static final int MAX_LENGTH = 16384;
//  private static final int MIN_BITS = 1;
//  private static final int MAX_BITS = 14;
//  private static final int[][] RESERVED_BITS = new int[MAX_BITS][];
//  private static final Complex[][][] COMPLEX_ROTATION = new Complex[MAX_BITS][ 2][];
//  
//  // Get array, indicating which data members should be swapped before FFT
//  private static int[] getReversedBits(int numberOfBits) {
//      if ((numberOfBits < MIN_BITS) || (numberOfBits > MAX_BITS))
//          throw new IllegalArgumentException();
//
//      // check if the array is already calculated
//      if (RESERVED_BITS[numberOfBits - 1] == null) {
//          int n = pow2(numberOfBits);
//          int[] rBits = new int[n];
//
//          // calculate the array
//          for (int i = 0; i < n; i++) {
//              int oldBits = i;
//              int newBits = 0;
//
//              for (int j = 0; j < numberOfBits; j++) {
//                newBits = (newBits << 1) | (oldBits & 1);
//                oldBits = (oldBits >> 1);
//              }
//              rBits[i] = newBits;
//          }
//          RESERVED_BITS[numberOfBits - 1] = rBits;
//      }
//      return RESERVED_BITS[numberOfBits - 1];
//  }
//  
//  // Reorder data for FFT using
//  private static void reorderData(Complex[] data) {
//    int len = data.length;
//    
//    // check data length
//    if ((len < MIN_LENGTH) || (len > MAX_LENGTH) || (!isPowerOf2(len))) {
//      throw new IllegalArgumentException("Incorrect data length.");
//    }
//    
//    int[] rBits = getReversedBits(log2(len));
//
//    for (int i = 0; i < len; i++) {
//      int s = rBits[i];
//      if (s > i) {
//        Complex t = data[i];
//        data[i] = data[s];
//        data[s] = t;
//      }
//    }
//  }
  
}