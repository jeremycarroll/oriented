/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.sf.oriented.omi.Label;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Export a drawing to excel to do the stretching part!
 *
 */
public class ExcelExport {
    

    private final Workbook workbook = new XSSFWorkbook();

    private final CreationHelper createHelper = workbook.getCreationHelper();
    private final EuclideanPseudoLines epl;

    private final Font subscript;
    private final Font bold;
    private final CellStyle boldStyle;

    private int startOfTransposedSection;

    public ExcelExport(EuclideanPseudoLines epl) {
        this.epl= epl;
        subscript = workbook.createFont();
        subscript.setTypeOffset(Font.SS_SUB);
        bold = workbook.createFont();
        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        boldStyle = workbook.createCellStyle();
        boldStyle.setFont(bold);
        
    }

    public void make() {
        createDataSheet();
        createAnglesSheet();
//        createSinesSheet();
//        createSinesTextSheet();
//        createChirotopeSheet();
//        createDifficultiesSheet();
//        createStartHereSheet();
    }
    private void createDataSheet() {
        Sheet sheet = workbook.createSheet("data");
        Row row = sheet.createRow((short)0);
        
        createCells(row, boldStyle, "Label","r","R","θ","Degrees","Radians");
        
        
        double step = 180/(elements().length-1);
        double deg = 0;
        for (int i=1;i< elements().length;i++, deg+= step) {
            Label lbl= elements()[i];
            Row r = sheet.createRow(i);
            String label = lbl.label();
            r.createCell(0).setCellValue(label);
            r.createCell(1).setCellValue(rts(r(i))); 
            r.createCell(2).setCellValue(1.0);
            r.createCell(3).setCellValue(rts(theta(i)));
            r.createCell(4).setCellValue((int)deg);
            r.createCell(5).setCellFormula("E"+(i+1)+"*PI()/180");
        }
        
        startOfTransposedSection = elements().length+2;
        
        for (int i=0;i<6;i++) {
            Row r = sheet.createRow(startOfTransposedSection+i);
            for (int j=0;j<elements().length;j++) {
                if (j>0) {
                    switch (i) {
                    case 1:
                        r.createCell(j).setCellValue(rts(r(i)));
                        continue;
                    case 3:
                        r.createCell(j).setCellValue(rts(theta(i)));
                        continue;
                    default:
                        // fall through
                    }
                }
                r.createCell(j).setCellFormula(abs(i, j));
            }
            r.getCell(0).setCellStyle(boldStyle);
        }
    }

    protected Label[] elements() {
        return epl.getEquivalentOM().elements();
    }

    private void createAnglesSheet() {
        Sheet sheet = workbook.createSheet("angles");
        set(sheet,0,0,boldStyle,"Angles");
        for (int i=1;i<elements().length;i++) {
            set(sheet,0,1+i,rts(theta(i)));
            set(sheet,1+i,0,rts(theta(i)));
            setformula(sheet,1,1+i,"data!"+absrel(5,i));
            setformula(sheet,1+i,1,"data!"+relabs(i,startOfTransposedSection+5));
            for (int j=1;j<elements().length;j++) {
                setformula(sheet,i+1,j+1,relabs(i+1,1)+"-"+absrel(1,j+1));
            }
        }
    }

    private void set(Sheet sheet, int i, int j, CellStyle style, String content) {
        Cell c = getOrCreate(sheet,i,j);
        c.setCellValue(content);
        c.setCellStyle(style);
    }

    private void setformula(Sheet sheet, int i, int j, String formula) {
        getOrCreate(sheet,i,j).setCellFormula(formula);
    }

    private void set(Sheet sheet, int i, int j, RichTextString rts) {
        Cell c = getOrCreate(sheet,i,j);
        c.setCellValue(rts);
        
    }

    private Cell getOrCreate(Sheet sheet, int i, int j) {
        Row r = sheet.getRow(j);
        if (r == null) {
            r = sheet.createRow(j);
        }
        Cell c = r.getCell(i);
        if (c == null) {
            c = r.createCell(i);
        }
        return c;
    }

    private void createCells(Row row, CellStyle st, String ...text ) {
        for (int i=0;i<text.length;i++) {
            if (text[i] != null) {
                Cell cell = row.createCell(i);
                cell.setCellValue(text[i]);
                if (st != null) {
                    cell.setCellStyle(st);
                }
            }
        }
        
    }

    private String absrel(int i, int j) {
        return "$"+a2z(i)+(j+1);
    }
    private String relabs(int i, int j) {
        return a2z(i)+"$"+(j+1);
    }
    protected String abs(int i, int j) {
        return "$"+a2z(i)+"$"+(j+1);
    }

    /**
     * This method converts an array of strings
     * interpreted as normal text, subscript text, alternating,
     * starting with normal text, into RTS
     * @param lbl
     * @return style rich text
     */
    private RichTextString rts(String label[]) {
        StringBuffer v = new StringBuffer();
        for (String p:label) {
            v.append(p);
        }
        RichTextString rts = createHelper.createRichTextString(v.toString());
        int pos = 0;
        boolean sub = false;
        for (String p:label) {
            int nextPos = pos + p.length();
            if (sub) {
                rts.applyFont(pos,nextPos, subscript);
            }
            sub = !sub;
            pos = nextPos;
        }
        return rts;
    }
    
    /**
     * This method operates with arrays of string
     * interpreted as normal text, subscript text, alternating,
     * starting with normal text.
     * @param a
     * @param b
     * @return a followed by b
     */
    private String[] _append(String a[], String b[]) {
        if (a.length%2 == 0) {
            String r[] = new String[a.length+b.length];
            System.arraycopy(a, 0, r, 0, a.length);
            System.arraycopy(b, 0, r, a.length, b.length);
            return r;
        } else {
            String r[] = new String[a.length+b.length-1];
            System.arraycopy(a, 0, r, 0, a.length-1);
            System.arraycopy(b, 1, r, a.length, b.length-1);
            r[a.length-1] = a[a.length-1] + b[0];
            return r;
        }
    }
    
    private String[] append(String[] ... a) {
        switch (a.length) {
        case 0:
            return new String[0];
        case 1:
            return a[0];
        case 2:
            return _append(a[0],a[1]);
        default:
            String r[] = _append(a[0],a[1]);
            for (int i=2;i<a.length;i++) {
                r = _append(r,a[i]);
            }
            return r;
        }
    }
    private String[] r(int i) {
        return new String[]{"r", elements()[i].label() };
    }

    private String[] theta(int i) {
        return new String[]{"θ", elements()[i].label() };
    }

    private String a2z(int i) {
        StringBuffer rslt = new StringBuffer();
        if (i<26) {
            rslt.append((char)('A'+i));
        } else {
            rslt.append((char)('A'+(i/26)-1));
            rslt.append((char)('A'+(i%26)));
        }
        return rslt.toString();
    }

    public void save(String fname) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(fname);
        workbook.write(fileOut);
        fileOut.close();
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
