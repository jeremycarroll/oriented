/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


class ParallelLex extends Lexicographic {
    private static ForkJoinPool forkJoinPool = new ForkJoinPool();
    private static class LexTask extends RecursiveAction {
        final ParallelLex lex;
        final int from;
        final int to;
        LexTask(ParallelLex lex, int from, int to) {
            this.lex = lex;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void compute() {
            if (to - from < lex.threshold ) {
                lex.markNonMinimal(from, to);
            } else {
                int mid = from + (to - from)/2;
                invokeAll(new LexTask(lex,from,mid), new LexTask(lex,mid,to));
            }
        }
        
    }
    private static final int THRESHOLD = 5000;
    private int threshold;

    @Override
    void markNonMinimal() {
        if (sorted.length<THRESHOLD) {
            super.markNonMinimal();
        } else {
            final int nProc = Runtime.getRuntime().availableProcessors();
            threshold = (sorted.length + nProc -1 ) / nProc;
            if (threshold<THRESHOLD) {
                threshold = THRESHOLD;
            }
            forkJoinPool.invoke(new LexTask(this, 0, sorted.length));
        }
    }


}


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
