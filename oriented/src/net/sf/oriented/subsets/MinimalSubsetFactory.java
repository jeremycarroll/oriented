/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.BitSet;
import java.util.Collection;


public class MinimalSubsetFactory {

    public static MinimalSubsets naive() {
        
        return new NaiveMinimalSubsets();
    }
    
    /**
     * Use an implementation based on:
     * <a href="http://stackoverflow.com/a/8996897/2276263">Aaron McDaid's comment</a>:
     * @return
     */
    public static MinimalSubsets mcdaid() {
        
        return new TransposeMinimalSubsets();
    }
    
    public static MinimalSubsets amsCard() {
        return new AMSCard();
    }

    public static MinimalSubsets amsLex() {
//      return   new MinimalSubsets() {
//      
//
//      @Override
//      public List<BitSet> minimal(Collection<BitSet> full) {
//          MinimalSubsets sat =  new AMSLex();
//          MinimalSubsets mcd = mcdaid();
//          
//          final List<BitSet> v1 = sat.minimal(full);
//          System.err.println(v1.size());
//          return mcd.minimal(v1);
//      }
//      };
      

        return new AMSLex();
    }
    static int max(Collection<BitSet> full) {
        int m = 0;
        for (BitSet b:full) {
            int l =  b.length();
            if (l>m) {
                m = l;
            }
        }
        return m;
    }

}
/*
naive-CompressBitMaps-circularsaw5*0-bits-230-sets-1-423 287 answers
naive-CompressBitMaps-circularsaw5*0-bits-230-sets-1-423 22
naive-Pritchard-circularsaw5*0-bits-230-sets-1-423 287 answers
naive-Pritchard-circularsaw5*0-bits-230-sets-1-423 10
naive-ReversePritchard-circularsaw5*0-bits-230-sets-1-423 287 answers
naive-ReversePritchard-circularsaw5*0-bits-230-sets-1-423 4
naive-Minimal-circularsaw5*0-bits-230-sets-1-423 287 answers
naive-Minimal-circularsaw5*0-bits-230-sets-1-423 3
mcdaid-CompressBitMaps-circularsaw5*0-bits-230-sets-1-423 287 answers
mcdaid-CompressBitMaps-circularsaw5*0-bits-230-sets-1-423 56
mcdaid-Pritchard-circularsaw5*0-bits-230-sets-1-423 287 answers
mcdaid-Pritchard-circularsaw5*0-bits-230-sets-1-423 15
mcdaid-ReversePritchard-circularsaw5*0-bits-230-sets-1-423 287 answers
mcdaid-ReversePritchard-circularsaw5*0-bits-230-sets-1-423 7
mcdaid-Minimal-circularsaw5*0-bits-230-sets-1-423 287 answers
mcdaid-Minimal-circularsaw5*0-bits-230-sets-1-423 5
amsCard-CompressBitMaps-circularsaw5*0-bits-230-sets-1-423 287 answers
amsCard-CompressBitMaps-circularsaw5*0-bits-230-sets-1-423 3
amsCard-Pritchard-circularsaw5*0-bits-230-sets-1-423 287 answers
amsCard-Pritchard-circularsaw5*0-bits-230-sets-1-423 1
amsCard-ReversePritchard-circularsaw5*0-bits-230-sets-1-423 287 answers
amsCard-ReversePritchard-circularsaw5*0-bits-230-sets-1-423 2
amsCard-Minimal-circularsaw5*0-bits-230-sets-1-423 287 answers
amsCard-Minimal-circularsaw5*0-bits-230-sets-1-423 3
amsLex-CompressBitMaps-circularsaw5*0-bits-230-sets-1-423 287 answers
amsLex-CompressBitMaps-circularsaw5*0-bits-230-sets-1-423 34
amsLex-Pritchard-circularsaw5*0-bits-230-sets-1-423 287 answers
amsLex-Pritchard-circularsaw5*0-bits-230-sets-1-423 7
amsLex-ReversePritchard-circularsaw5*0-bits-230-sets-1-423 287 answers
amsLex-ReversePritchard-circularsaw5*0-bits-230-sets-1-423 7
amsLex-Minimal-circularsaw5*0-bits-230-sets-1-423 287 answers
amsLex-Minimal-circularsaw5*0-bits-230-sets-1-423 9
naive-CompressBitMaps-circularsaw5*A-bits-268-sets-2-61 49 answers
naive-CompressBitMaps-circularsaw5*A-bits-268-sets-2-61 0
naive-Pritchard-circularsaw5*A-bits-268-sets-2-61 49 answers
naive-Pritchard-circularsaw5*A-bits-268-sets-2-61 1
naive-ReversePritchard-circularsaw5*A-bits-268-sets-2-61 49 answers
naive-ReversePritchard-circularsaw5*A-bits-268-sets-2-61 0
naive-Minimal-circularsaw5*A-bits-268-sets-2-61 49 answers
naive-Minimal-circularsaw5*A-bits-268-sets-2-61 0
mcdaid-CompressBitMaps-circularsaw5*A-bits-268-sets-2-61 49 answers
mcdaid-CompressBitMaps-circularsaw5*A-bits-268-sets-2-61 0
mcdaid-Pritchard-circularsaw5*A-bits-268-sets-2-61 49 answers
mcdaid-Pritchard-circularsaw5*A-bits-268-sets-2-61 1
mcdaid-ReversePritchard-circularsaw5*A-bits-268-sets-2-61 49 answers
mcdaid-ReversePritchard-circularsaw5*A-bits-268-sets-2-61 0
mcdaid-Minimal-circularsaw5*A-bits-268-sets-2-61 49 answers
mcdaid-Minimal-circularsaw5*A-bits-268-sets-2-61 0
amsCard-CompressBitMaps-circularsaw5*A-bits-268-sets-2-61 49 answers
amsCard-CompressBitMaps-circularsaw5*A-bits-268-sets-2-61 0
amsCard-Pritchard-circularsaw5*A-bits-268-sets-2-61 49 answers
amsCard-Pritchard-circularsaw5*A-bits-268-sets-2-61 0
amsCard-ReversePritchard-circularsaw5*A-bits-268-sets-2-61 49 answers
amsCard-ReversePritchard-circularsaw5*A-bits-268-sets-2-61 0
amsCard-Minimal-circularsaw5*A-bits-268-sets-2-61 49 answers
amsCard-Minimal-circularsaw5*A-bits-268-sets-2-61 0
amsLex-CompressBitMaps-circularsaw5*A-bits-268-sets-2-61 49 answers
amsLex-CompressBitMaps-circularsaw5*A-bits-268-sets-2-61 1
amsLex-Pritchard-circularsaw5*A-bits-268-sets-2-61 49 answers
amsLex-Pritchard-circularsaw5*A-bits-268-sets-2-61 0
amsLex-ReversePritchard-circularsaw5*A-bits-268-sets-2-61 49 answers
amsLex-ReversePritchard-circularsaw5*A-bits-268-sets-2-61 1
amsLex-Minimal-circularsaw5*A-bits-268-sets-2-61 49 answers
amsLex-Minimal-circularsaw5*A-bits-268-sets-2-61 0
naive-CompressBitMaps-_saw5A*0-bits-265-sets-3-315 262 answers
naive-CompressBitMaps-_saw5A*0-bits-265-sets-3-315 1
naive-Pritchard-_saw5A*0-bits-265-sets-3-315 262 answers
naive-Pritchard-_saw5A*0-bits-265-sets-3-315 1
naive-ReversePritchard-_saw5A*0-bits-265-sets-3-315 262 answers
naive-ReversePritchard-_saw5A*0-bits-265-sets-3-315 1
naive-Minimal-_saw5A*0-bits-265-sets-3-315 262 answers
naive-Minimal-_saw5A*0-bits-265-sets-3-315 0
mcdaid-CompressBitMaps-_saw5A*0-bits-265-sets-3-315 262 answers
mcdaid-CompressBitMaps-_saw5A*0-bits-265-sets-3-315 1
mcdaid-Pritchard-_saw5A*0-bits-265-sets-3-315 262 answers
mcdaid-Pritchard-_saw5A*0-bits-265-sets-3-315 2
mcdaid-ReversePritchard-_saw5A*0-bits-265-sets-3-315 262 answers
mcdaid-ReversePritchard-_saw5A*0-bits-265-sets-3-315 1
mcdaid-Minimal-_saw5A*0-bits-265-sets-3-315 262 answers
mcdaid-Minimal-_saw5A*0-bits-265-sets-3-315 1
amsCard-CompressBitMaps-_saw5A*0-bits-265-sets-3-315 262 answers
amsCard-CompressBitMaps-_saw5A*0-bits-265-sets-3-315 1
amsCard-Pritchard-_saw5A*0-bits-265-sets-3-315 262 answers
amsCard-Pritchard-_saw5A*0-bits-265-sets-3-315 1
amsCard-ReversePritchard-_saw5A*0-bits-265-sets-3-315 262 answers
amsCard-ReversePritchard-_saw5A*0-bits-265-sets-3-315 1
amsCard-Minimal-_saw5A*0-bits-265-sets-3-315 262 answers
amsCard-Minimal-_saw5A*0-bits-265-sets-3-315 1
amsLex-CompressBitMaps-_saw5A*0-bits-265-sets-3-315 262 answers
amsLex-CompressBitMaps-_saw5A*0-bits-265-sets-3-315 1
amsLex-Pritchard-_saw5A*0-bits-265-sets-3-315 262 answers
amsLex-Pritchard-_saw5A*0-bits-265-sets-3-315 1
amsLex-ReversePritchard-_saw5A*0-bits-265-sets-3-315 262 answers
amsLex-ReversePritchard-_saw5A*0-bits-265-sets-3-315 1
amsLex-Minimal-_saw5A*0-bits-265-sets-3-315 262 answers
amsLex-Minimal-_saw5A*0-bits-265-sets-3-315 1
naive-CompressBitMaps-_deformSaw5*0-bits-235-sets-4-377 263 answers
naive-CompressBitMaps-_deformSaw5*0-bits-235-sets-4-377 1
naive-Pritchard-_deformSaw5*0-bits-235-sets-4-377 263 answers
naive-Pritchard-_deformSaw5*0-bits-235-sets-4-377 1
naive-ReversePritchard-_deformSaw5*0-bits-235-sets-4-377 263 answers
naive-ReversePritchard-_deformSaw5*0-bits-235-sets-4-377 1
naive-Minimal-_deformSaw5*0-bits-235-sets-4-377 263 answers
naive-Minimal-_deformSaw5*0-bits-235-sets-4-377 1
mcdaid-CompressBitMaps-_deformSaw5*0-bits-235-sets-4-377 263 answers
mcdaid-CompressBitMaps-_deformSaw5*0-bits-235-sets-4-377 2
mcdaid-Pritchard-_deformSaw5*0-bits-235-sets-4-377 263 answers
mcdaid-Pritchard-_deformSaw5*0-bits-235-sets-4-377 2
mcdaid-ReversePritchard-_deformSaw5*0-bits-235-sets-4-377 263 answers
mcdaid-ReversePritchard-_deformSaw5*0-bits-235-sets-4-377 1
mcdaid-Minimal-_deformSaw5*0-bits-235-sets-4-377 263 answers
mcdaid-Minimal-_deformSaw5*0-bits-235-sets-4-377 2
amsCard-CompressBitMaps-_deformSaw5*0-bits-235-sets-4-377 263 answers
amsCard-CompressBitMaps-_deformSaw5*0-bits-235-sets-4-377 1
amsCard-Pritchard-_deformSaw5*0-bits-235-sets-4-377 263 answers
amsCard-Pritchard-_deformSaw5*0-bits-235-sets-4-377 1
amsCard-ReversePritchard-_deformSaw5*0-bits-235-sets-4-377 263 answers
amsCard-ReversePritchard-_deformSaw5*0-bits-235-sets-4-377 1
amsCard-Minimal-_deformSaw5*0-bits-235-sets-4-377 263 answers
amsCard-Minimal-_deformSaw5*0-bits-235-sets-4-377 1
amsLex-CompressBitMaps-_deformSaw5*0-bits-235-sets-4-377 263 answers
amsLex-CompressBitMaps-_deformSaw5*0-bits-235-sets-4-377 2
amsLex-Pritchard-_deformSaw5*0-bits-235-sets-4-377 263 answers
amsLex-Pritchard-_deformSaw5*0-bits-235-sets-4-377 2
amsLex-ReversePritchard-_deformSaw5*0-bits-235-sets-4-377 263 answers
amsLex-ReversePritchard-_deformSaw5*0-bits-235-sets-4-377 2
amsLex-Minimal-_deformSaw5*0-bits-235-sets-4-377 263 answers
amsLex-Minimal-_deformSaw5*0-bits-235-sets-4-377 2
naive-CompressBitMaps-_deformSaw5*A-bits-276-sets-5-56 51 answers
naive-CompressBitMaps-_deformSaw5*A-bits-276-sets-5-56 1
naive-Pritchard-_deformSaw5*A-bits-276-sets-5-56 51 answers
naive-Pritchard-_deformSaw5*A-bits-276-sets-5-56 0
naive-ReversePritchard-_deformSaw5*A-bits-276-sets-5-56 51 answers
naive-ReversePritchard-_deformSaw5*A-bits-276-sets-5-56 0
naive-Minimal-_deformSaw5*A-bits-276-sets-5-56 51 answers
naive-Minimal-_deformSaw5*A-bits-276-sets-5-56 0
mcdaid-CompressBitMaps-_deformSaw5*A-bits-276-sets-5-56 51 answers
mcdaid-CompressBitMaps-_deformSaw5*A-bits-276-sets-5-56 0
mcdaid-Pritchard-_deformSaw5*A-bits-276-sets-5-56 51 answers
mcdaid-Pritchard-_deformSaw5*A-bits-276-sets-5-56 0
mcdaid-ReversePritchard-_deformSaw5*A-bits-276-sets-5-56 51 answers
mcdaid-ReversePritchard-_deformSaw5*A-bits-276-sets-5-56 0
mcdaid-Minimal-_deformSaw5*A-bits-276-sets-5-56 51 answers
mcdaid-Minimal-_deformSaw5*A-bits-276-sets-5-56 0
amsCard-CompressBitMaps-_deformSaw5*A-bits-276-sets-5-56 51 answers
amsCard-CompressBitMaps-_deformSaw5*A-bits-276-sets-5-56 0
amsCard-Pritchard-_deformSaw5*A-bits-276-sets-5-56 51 answers
amsCard-Pritchard-_deformSaw5*A-bits-276-sets-5-56 1
amsCard-ReversePritchard-_deformSaw5*A-bits-276-sets-5-56 51 answers
amsCard-ReversePritchard-_deformSaw5*A-bits-276-sets-5-56 0
amsCard-Minimal-_deformSaw5*A-bits-276-sets-5-56 51 answers
amsCard-Minimal-_deformSaw5*A-bits-276-sets-5-56 0
amsLex-CompressBitMaps-_deformSaw5*A-bits-276-sets-5-56 51 answers
amsLex-CompressBitMaps-_deformSaw5*A-bits-276-sets-5-56 1
amsLex-Pritchard-_deformSaw5*A-bits-276-sets-5-56 51 answers
amsLex-Pritchard-_deformSaw5*A-bits-276-sets-5-56 0
amsLex-ReversePritchard-_deformSaw5*A-bits-276-sets-5-56 51 answers
amsLex-ReversePritchard-_deformSaw5*A-bits-276-sets-5-56 0
amsLex-Minimal-_deformSaw5*A-bits-276-sets-5-56 51 answers
amsLex-Minimal-_deformSaw5*A-bits-276-sets-5-56 1
mcdaid-CompressBitMaps-_deformSaw5*B-bits-258-sets-6-73 51 answers
mcdaid-CompressBitMaps-_deformSaw5*B-bits-258-sets-6-73 0
mcdaid-Pritchard-_deformSaw5*B-bits-258-sets-6-73 51 answers
mcdaid-Pritchard-_deformSaw5*B-bits-258-sets-6-73 1
mcdaid-ReversePritchard-_deformSaw5*B-bits-258-sets-6-73 51 answers
mcdaid-ReversePritchard-_deformSaw5*B-bits-258-sets-6-73 0
mcdaid-Minimal-_deformSaw5*B-bits-258-sets-6-73 51 answers
mcdaid-Minimal-_deformSaw5*B-bits-258-sets-6-73 0
amsCard-CompressBitMaps-_deformSaw5*B-bits-258-sets-6-73 51 answers
amsCard-CompressBitMaps-_deformSaw5*B-bits-258-sets-6-73 0
amsCard-Pritchard-_deformSaw5*B-bits-258-sets-6-73 51 answers
amsCard-Pritchard-_deformSaw5*B-bits-258-sets-6-73 0
amsCard-ReversePritchard-_deformSaw5*B-bits-258-sets-6-73 51 answers
amsCard-ReversePritchard-_deformSaw5*B-bits-258-sets-6-73 1
amsCard-Minimal-_deformSaw5*B-bits-258-sets-6-73 51 answers
amsCard-Minimal-_deformSaw5*B-bits-258-sets-6-73 0
amsLex-CompressBitMaps-_deformSaw5*B-bits-258-sets-6-73 51 answers
amsLex-CompressBitMaps-_deformSaw5*B-bits-258-sets-6-73 1
amsLex-Pritchard-_deformSaw5*B-bits-258-sets-6-73 51 answers
amsLex-Pritchard-_deformSaw5*B-bits-258-sets-6-73 0
amsLex-ReversePritchard-_deformSaw5*B-bits-258-sets-6-73 51 answers
amsLex-ReversePritchard-_deformSaw5*B-bits-258-sets-6-73 1
amsLex-Minimal-_deformSaw5*B-bits-258-sets-6-73 51 answers
amsLex-Minimal-_deformSaw5*B-bits-258-sets-6-73 1
naive-CompressBitMaps-pappus*0-bits-74-sets-7-8 7 answers
naive-CompressBitMaps-pappus*0-bits-74-sets-7-8 0
naive-Pritchard-pappus*0-bits-74-sets-7-8 7 answers
naive-Pritchard-pappus*0-bits-74-sets-7-8 0
naive-ReversePritchard-pappus*0-bits-74-sets-7-8 7 answers
naive-ReversePritchard-pappus*0-bits-74-sets-7-8 0
naive-Minimal-pappus*0-bits-74-sets-7-8 7 answers
naive-Minimal-pappus*0-bits-74-sets-7-8 0
mcdaid-CompressBitMaps-pappus*0-bits-74-sets-7-8 7 answers
mcdaid-CompressBitMaps-pappus*0-bits-74-sets-7-8 0
mcdaid-Pritchard-pappus*0-bits-74-sets-7-8 7 answers
mcdaid-Pritchard-pappus*0-bits-74-sets-7-8 0
mcdaid-ReversePritchard-pappus*0-bits-74-sets-7-8 7 answers
mcdaid-ReversePritchard-pappus*0-bits-74-sets-7-8 0
mcdaid-Minimal-pappus*0-bits-74-sets-7-8 7 answers
mcdaid-Minimal-pappus*0-bits-74-sets-7-8 0
amsCard-CompressBitMaps-pappus*0-bits-74-sets-7-8 7 answers
amsCard-CompressBitMaps-pappus*0-bits-74-sets-7-8 0
amsCard-Pritchard-pappus*0-bits-74-sets-7-8 7 answers
amsCard-Pritchard-pappus*0-bits-74-sets-7-8 0
amsCard-ReversePritchard-pappus*0-bits-74-sets-7-8 7 answers
amsCard-ReversePritchard-pappus*0-bits-74-sets-7-8 0
amsCard-Minimal-pappus*0-bits-74-sets-7-8 7 answers
amsCard-Minimal-pappus*0-bits-74-sets-7-8 0
amsLex-CompressBitMaps-pappus*0-bits-74-sets-7-8 7 answers
amsLex-CompressBitMaps-pappus*0-bits-74-sets-7-8 0
amsLex-Pritchard-pappus*0-bits-74-sets-7-8 7 answers
amsLex-Pritchard-pappus*0-bits-74-sets-7-8 0
amsLex-ReversePritchard-pappus*0-bits-74-sets-7-8 7 answers
amsLex-ReversePritchard-pappus*0-bits-74-sets-7-8 0
amsLex-Minimal-pappus*0-bits-74-sets-7-8 7 answers
amsLex-Minimal-pappus*0-bits-74-sets-7-8 0
naive-CompressBitMaps-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
naive-CompressBitMaps-tsukamoto13.+1*A-bits-360-sets-8-15362 640
naive-Pritchard-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
naive-Pritchard-tsukamoto13.+1*A-bits-360-sets-8-15362 723
naive-ReversePritchard-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
naive-ReversePritchard-tsukamoto13.+1*A-bits-360-sets-8-15362 712
naive-Minimal-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
naive-Minimal-tsukamoto13.+1*A-bits-360-sets-8-15362 580
mcdaid-CompressBitMaps-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
mcdaid-CompressBitMaps-tsukamoto13.+1*A-bits-360-sets-8-15362 191
mcdaid-Pritchard-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
mcdaid-Pritchard-tsukamoto13.+1*A-bits-360-sets-8-15362 188
mcdaid-ReversePritchard-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
mcdaid-ReversePritchard-tsukamoto13.+1*A-bits-360-sets-8-15362 191
mcdaid-Minimal-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
mcdaid-Minimal-tsukamoto13.+1*A-bits-360-sets-8-15362 189
amsCard-CompressBitMaps-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
amsCard-CompressBitMaps-tsukamoto13.+1*A-bits-360-sets-8-15362 67
amsCard-Pritchard-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
amsCard-Pritchard-tsukamoto13.+1*A-bits-360-sets-8-15362 22
amsCard-ReversePritchard-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
amsCard-ReversePritchard-tsukamoto13.+1*A-bits-360-sets-8-15362 23
amsCard-Minimal-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
amsCard-Minimal-tsukamoto13.+1*A-bits-360-sets-8-15362 55
amsLex-CompressBitMaps-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
amsLex-CompressBitMaps-tsukamoto13.+1*A-bits-360-sets-8-15362 199
amsLex-Pritchard-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
amsLex-Pritchard-tsukamoto13.+1*A-bits-360-sets-8-15362 190
amsLex-ReversePritchard-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
amsLex-ReversePritchard-tsukamoto13.+1*A-bits-360-sets-8-15362 115
amsLex-Minimal-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
amsLex-Minimal-tsukamoto13.+1*A-bits-360-sets-8-15362 203
naive-CompressBitMaps-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
naive-CompressBitMaps-tsukamoto13.-1*A-bits-364-sets-9-23369 1267
naive-Pritchard-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
naive-Pritchard-tsukamoto13.-1*A-bits-364-sets-9-23369 1648
naive-ReversePritchard-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
naive-ReversePritchard-tsukamoto13.-1*A-bits-364-sets-9-23369 1693
naive-Minimal-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
naive-Minimal-tsukamoto13.-1*A-bits-364-sets-9-23369 1064
mcdaid-CompressBitMaps-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
mcdaid-CompressBitMaps-tsukamoto13.-1*A-bits-364-sets-9-23369 335
mcdaid-Pritchard-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
mcdaid-Pritchard-tsukamoto13.-1*A-bits-364-sets-9-23369 330
mcdaid-ReversePritchard-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
mcdaid-ReversePritchard-tsukamoto13.-1*A-bits-364-sets-9-23369 342
mcdaid-Minimal-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
mcdaid-Minimal-tsukamoto13.-1*A-bits-364-sets-9-23369 345
amsCard-CompressBitMaps-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
amsCard-CompressBitMaps-tsukamoto13.-1*A-bits-364-sets-9-23369 115
amsCard-Pritchard-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
amsCard-Pritchard-tsukamoto13.-1*A-bits-364-sets-9-23369 40
amsCard-ReversePritchard-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
amsCard-ReversePritchard-tsukamoto13.-1*A-bits-364-sets-9-23369 38
amsCard-Minimal-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
amsCard-Minimal-tsukamoto13.-1*A-bits-364-sets-9-23369 87
amsLex-CompressBitMaps-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
amsLex-CompressBitMaps-tsukamoto13.-1*A-bits-364-sets-9-23369 239
amsLex-Pritchard-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
amsLex-Pritchard-tsukamoto13.-1*A-bits-364-sets-9-23369 183
amsLex-ReversePritchard-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
amsLex-ReversePritchard-tsukamoto13.-1*A-bits-364-sets-9-23369 183
amsLex-Minimal-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
amsLex-Minimal-tsukamoto13.-1*A-bits-364-sets-9-23369 240
mcdaid-CompressBitMaps-tsukamoto13.+1*B-bits-488-sets-10-1145894 396095 answers
mcdaid-CompressBitMaps-tsukamoto13.+1*B-bits-488-sets-10-1145894 92733
mcdaid-Pritchard-tsukamoto13.+1*B-bits-488-sets-10-1145894 396095 answers
mcdaid-Pritchard-tsukamoto13.+1*B-bits-488-sets-10-1145894 96333
mcdaid-ReversePritchard-tsukamoto13.+1*B-bits-488-sets-10-1145894 396095 answers
mcdaid-ReversePritchard-tsukamoto13.+1*B-bits-488-sets-10-1145894 93544
mcdaid-Minimal-tsukamoto13.+1*B-bits-488-sets-10-1145894 396095 answers
mcdaid-Minimal-tsukamoto13.+1*B-bits-488-sets-10-1145894 91646
amsCard-CompressBitMaps-tsukamoto13.+1*B-bits-488-sets-10-1145894 396095 answers
amsCard-CompressBitMaps-tsukamoto13.+1*B-bits-488-sets-10-1145894 463178
amsCard-Pritchard-tsukamoto13.+1*B-bits-488-sets-10-1145894 396095 answers
amsCard-Pritchard-tsukamoto13.+1*B-bits-488-sets-10-1145894 50769
amsCard-ReversePritchard-tsukamoto13.+1*B-bits-488-sets-10-1145894 396095 answers
amsCard-ReversePritchard-tsukamoto13.+1*B-bits-488-sets-10-1145894 49276
amsCard-Minimal-tsukamoto13.+1*B-bits-488-sets-10-1145894 396095 answers
amsCard-Minimal-tsukamoto13.+1*B-bits-488-sets-10-1145894 277511
amsLex-CompressBitMaps-tsukamoto13.+1*B-bits-488-sets-10-1145894 396095 answers
amsLex-CompressBitMaps-tsukamoto13.+1*B-bits-488-sets-10-1145894 39062
amsLex-Pritchard-tsukamoto13.+1*B-bits-488-sets-10-1145894 396095 answers
amsLex-Pritchard-tsukamoto13.+1*B-bits-488-sets-10-1145894 29233
amsLex-ReversePritchard-tsukamoto13.+1*B-bits-488-sets-10-1145894 396095 answers
amsLex-ReversePritchard-tsukamoto13.+1*B-bits-488-sets-10-1145894 28583
amsLex-Minimal-tsukamoto13.+1*B-bits-488-sets-10-1145894 396095 answers
amsLex-Minimal-tsukamoto13.+1*B-bits-488-sets-10-1145894 35884
mcdaid-CompressBitMaps-tsukamoto13.-1*B-bits-488-sets-11-1581601 505652 answers
mcdaid-CompressBitMaps-tsukamoto13.-1*B-bits-488-sets-11-1581601 185831
mcdaid-Pritchard-tsukamoto13.-1*B-bits-488-sets-11-1581601 505652 answers
mcdaid-Pritchard-tsukamoto13.-1*B-bits-488-sets-11-1581601 190070
mcdaid-ReversePritchard-tsukamoto13.-1*B-bits-488-sets-11-1581601 505652 answers
mcdaid-ReversePritchard-tsukamoto13.-1*B-bits-488-sets-11-1581601 180590
mcdaid-Minimal-tsukamoto13.-1*B-bits-488-sets-11-1581601 505652 answers
mcdaid-Minimal-tsukamoto13.-1*B-bits-488-sets-11-1581601 181366
amsCard-CompressBitMaps-tsukamoto13.-1*B-bits-488-sets-11-1581601 505652 answers
amsCard-CompressBitMaps-tsukamoto13.-1*B-bits-488-sets-11-1581601 1419192
amsCard-Pritchard-tsukamoto13.-1*B-bits-488-sets-11-1581601 505652 answers
amsCard-Pritchard-tsukamoto13.-1*B-bits-488-sets-11-1581601 90785
amsCard-ReversePritchard-tsukamoto13.-1*B-bits-488-sets-11-1581601 505652 answers
amsCard-ReversePritchard-tsukamoto13.-1*B-bits-488-sets-11-1581601 95009
amsCard-Minimal-tsukamoto13.-1*B-bits-488-sets-11-1581601 505652 answers
amsCard-Minimal-tsukamoto13.-1*B-bits-488-sets-11-1581601 840111
amsLex-CompressBitMaps-tsukamoto13.-1*B-bits-488-sets-11-1581601 505652 answers
amsLex-CompressBitMaps-tsukamoto13.-1*B-bits-488-sets-11-1581601 87748
amsLex-Pritchard-tsukamoto13.-1*B-bits-488-sets-11-1581601 505652 answers
amsLex-Pritchard-tsukamoto13.-1*B-bits-488-sets-11-1581601 54964
amsLex-ReversePritchard-tsukamoto13.-1*B-bits-488-sets-11-1581601 505652 answers
amsLex-ReversePritchard-tsukamoto13.-1*B-bits-488-sets-11-1581601 50573
amsLex-Minimal-tsukamoto13.-1*B-bits-488-sets-11-1581601 505652 answers
amsLex-Minimal-tsukamoto13.-1*B-bits-488-sets-11-1581601 80016
naive-CompressBitMaps-ringel*0-bits-90-sets-12-16 16 answers
naive-CompressBitMaps-ringel*0-bits-90-sets-12-16 0
naive-Pritchard-ringel*0-bits-90-sets-12-16 16 answers
naive-Pritchard-ringel*0-bits-90-sets-12-16 0
naive-ReversePritchard-ringel*0-bits-90-sets-12-16 16 answers
naive-ReversePritchard-ringel*0-bits-90-sets-12-16 0
naive-Minimal-ringel*0-bits-90-sets-12-16 16 answers
naive-Minimal-ringel*0-bits-90-sets-12-16 0
mcdaid-CompressBitMaps-ringel*0-bits-90-sets-12-16 16 answers
mcdaid-CompressBitMaps-ringel*0-bits-90-sets-12-16 0
mcdaid-Pritchard-ringel*0-bits-90-sets-12-16 16 answers
mcdaid-Pritchard-ringel*0-bits-90-sets-12-16 0
mcdaid-ReversePritchard-ringel*0-bits-90-sets-12-16 16 answers
mcdaid-ReversePritchard-ringel*0-bits-90-sets-12-16 0
mcdaid-Minimal-ringel*0-bits-90-sets-12-16 16 answers
mcdaid-Minimal-ringel*0-bits-90-sets-12-16 0
amsCard-CompressBitMaps-ringel*0-bits-90-sets-12-16 16 answers
amsCard-CompressBitMaps-ringel*0-bits-90-sets-12-16 0
amsCard-Pritchard-ringel*0-bits-90-sets-12-16 16 answers
amsCard-Pritchard-ringel*0-bits-90-sets-12-16 0
amsCard-ReversePritchard-ringel*0-bits-90-sets-12-16 16 answers
amsCard-ReversePritchard-ringel*0-bits-90-sets-12-16 0
amsCard-Minimal-ringel*0-bits-90-sets-12-16 16 answers
amsCard-Minimal-ringel*0-bits-90-sets-12-16 0
amsLex-CompressBitMaps-ringel*0-bits-90-sets-12-16 16 answers
amsLex-CompressBitMaps-ringel*0-bits-90-sets-12-16 0
amsLex-Pritchard-ringel*0-bits-90-sets-12-16 16 answers
amsLex-Pritchard-ringel*0-bits-90-sets-12-16 0
amsLex-ReversePritchard-ringel*0-bits-90-sets-12-16 16 answers
amsLex-ReversePritchard-ringel*0-bits-90-sets-12-16 0
amsLex-Minimal-ringel*0-bits-90-sets-12-16 16 answers
amsLex-Minimal-ringel*0-bits-90-sets-12-16 0

 */


/************************************************************************
    This file is part of the Java Oriented Matroid Library.  

    The Java Oriented Matroid Library is distributed in the hope that it 
    will be useful, but WITHOUT ANY WARRANTY; without even the implied 
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
    See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Java Oriented Matroid Library.  
    If not, see <http://www.gnu.org/licenses/>.

**************************************************************************/
