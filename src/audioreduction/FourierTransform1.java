/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioreduction;

public class FourierTransform1 {
//Complex[] TComplexArray;
int fwindowsSize , tTlbSize;
double[] fsinTbl,fcosTbl;

public void InitSinCos (){
 for (int i =1;i<=2*fwindowsSize;i++){
     fsinTbl[i]=(-1)*(Math.sin(Math.toRadians(180/i)));
     fcosTbl[i]=Math.cos(Math.toRadians(180/i));
     
 }   
}

public void FFT(Complex[] TComplexArray, int n, int m , boolean b  ){
    InitMem(n);
    InitSinCos();
    int Nv2 = n >> 1;
    int Nm1 = n-1;
    int I,ip,le,le1,k,j=1;
    double redummy , imdummy;
    Complex t,w,uo,u ;
    if (b){
        for( I =0; I<n;I++){
            TComplexArray[I].setIm(-TComplexArray[I].imag());
        }
    }
    for (I=1;I<=Nm1;I++){
        if (I<j){
           t = TComplexArray[j-1];//new Complex(TComplexArray[j-1].real(), TComplexArray[j-1].imag());
           TComplexArray[j-1]=TComplexArray[I-1];
           TComplexArray[I-1]=t;
        }
        k = Nv2;
        while (k<j){
            j-=k;
            k=k>>1;
        }
        j+=k;
    }
    for (int l=1;l<=m;l++){
        le  = 2<<(l-1);
        le1 = le>>1;
        u = new Complex(1.0, 0.0);
        w = new Complex(fcosTbl[le1], fsinTbl[le1]);
        for ( j= 1 ;j<=le1;j++){
            I = j ;
            while (I<n){
                ip = I+le1;
                t = new Complex(TComplexArray[ip-1].real()*u.real()-TComplexArray[ip-1].imag()*u.imag(), TComplexArray[ip-1].real()*u.imag()+TComplexArray[ip-1].imag()*u.real());
                TComplexArray[ip-1].setRe(TComplexArray[I-1].real()-t.real());
                TComplexArray[ip-1].setIm(TComplexArray[I-1].imag()-t.imag());
                TComplexArray[I-1].setRe(TComplexArray[I-1].real()+t.real());
                TComplexArray[I-1].setIm(TComplexArray[I-1].imag()+t.imag());
                I+=le;
            }
            uo=u;
            u.setRe((uo.real()*w.real())-(uo.imag()*w.imag()));
            u.setIm((uo.real()*w.imag())-(uo.imag()*w.real()));
        }
        
    }
    imdummy = 1/Math.sqrt(n);
    if (b){
        imdummy=-imdummy;
    }
    redummy= Math.abs(imdummy);
    for (I=1;I<n;I++){
        TComplexArray[I-1].setRe(TComplexArray[I-1].real()*redummy);
        TComplexArray[I-1].setIm(TComplexArray[I-1].imag()*imdummy);
    }
    if (!b){
     for (I=0;I<n-1;I++){ 
         
      TComplexArray[I].setRe(Math.sqrt(Math.pow(TComplexArray[I].real(),2)+Math.pow(TComplexArray[I].imag(),2)));   
       
    }
    
}

    
}


public void InitMem(int windowsSize){
  fwindowsSize=  windowsSize;
  tTlbSize = (2*fwindowsSize+1)*Double.SIZE;
  fsinTbl = new double[tTlbSize];
  fcosTbl = new double[tTlbSize];
}



}

