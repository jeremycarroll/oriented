/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.io.FileOutputStream;
import java.io.IOException;

import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OMasChirotope;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
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
    private final CellStyle signedStyle;

    private int startOfTransposedSection;

    public ExcelExport(EuclideanPseudoLines epl) {
        this.epl= epl;
        subscript = workbook.createFont();
        subscript.setTypeOffset(Font.SS_SUB);
        bold = workbook.createFont();
        bold.setBold(true);
        boldStyle = workbook.createCellStyle();
        boldStyle.setFont(bold);
        short signedDataFormat = workbook.createDataFormat().getFormat("+0;-0");
        signedStyle = workbook.createCellStyle();
        signedStyle.setDataFormat(signedDataFormat);
        
    }

    public void make() {
        createSolverSheet();
        createDataSheet();
        createAnglesSheet();
        createSinesSheet();
        createChirotopeSheet();
//        createDifficultiesSheet();
//        createStartHereSheet();
    }
    private void createSolverSheet() {
        Sheet sheet = workbook.createSheet("solver");
        createCells(sheet.createRow(0), boldStyle, "Solver: set-up, data and constraints");
        createCells(sheet.createRow(1), boldStyle, "","Set-up");
        createCells(sheet.createRow(3), boldStyle, "","","Angles");
        createCells(sheet.createRow(5), boldStyle, "","","Linear");
        createCells(sheet.createRow(7), boldStyle, "","","Beautify Outer Square");
        createCells(sheet.createRow(9), boldStyle, "","","Beautify Inner Square");
        createCells(sheet.createRow(11), boldStyle, "","","Beautify Outer Circle");
        createCells(sheet.createRow(13), boldStyle, "","","Beautify Inner Circle");

        createCells(sheet.createRow(15), boldStyle, "","Data");
        Row rRow = sheet.createRow(16);
        createCells(rRow, boldStyle, "","","r");
        Row aRow = sheet.createRow(17);
        createCells(aRow, boldStyle, "","","θ");
        Row dRow = sheet.createRow(19);

        double step = 180/(elements().length-1);
        double deg = 0;
        for (int i=1;i< elements().length;i++, deg+= step) {
            rRow.createCell(2+i).setCellValue(1);
            aRow.createCell(2+i).setCellValue((int)deg);
            dRow.createCell(2+i).setCellFormula(a2z(3+i)+"$18-"+a2z(2+i)+"$18-$D$19");
        }
        aRow.createCell(2+elements().length).setCellValue(180);

        Row eRow = sheet.createRow(18);
        createCells(eRow, boldStyle, "","","ε");
        eRow.createCell(3).setCellValue(0);
        
        createCells(sheet.createRow(21), boldStyle, "","Constraints");
        createCells(sheet.createRow(22), boldStyle, "Chirotope","","","","Difficulties");
        int lg = elements().length - 1;
        int chiCount = lg * (lg-1) / 2 * (lg-2) / 3;
        for (int i=0;i<chiCount;i++) {
            Row row = sheet.createRow(23+i);
            row.createCell(0).setCellFormula("chirotope!$G"+(i+3));
            row.createCell(1).setCellFormula("chirotope!$H"+(i+3));
        }
        
        
    }
    private void createDataSheet() {
        Sheet sheet = workbook.createSheet("data");
        Row row = sheet.createRow(0);
        
        createCells(row, boldStyle, "Label","r","R","θ","Degrees","Radians");
        
        for (int i=1;i< elements().length;i++) {
            Label lbl= elements()[i];
            Row r = sheet.createRow(i);
            String label = lbl.label();
            r.createCell(0).setCellValue(label);
            r.createCell(1).setCellValue(rts(r(i))); 
            r.createCell(2).setCellFormula("solver!$"+a2z(2+i)+"$17");
            r.createCell(3).setCellValue(rts(theta(i)));
            r.createCell(4).setCellFormula("solver!$"+a2z(2+i)+"$18");
            r.createCell(5).setCellFormula("E"+(i+1)+"*PI()/180");
        }
        Row rr = sheet.createRow(elements().length);
        rr.createCell(2).setCellValue("ε =");
        rr.createCell(4).setCellValue(0);
        rr.createCell(5).setCellFormula("E"+(elements().length+1));
        
        startOfTransposedSection = elements().length+3;
        
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

    private Label[] elements() {
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

    private void createSinesSheet() {
        Sheet sheet = workbook.createSheet("sines");
        set(sheet,0,0,boldStyle,"Sines");
        for (int i=1;i<elements().length;i++) {
            set(sheet,1,1+i,rts(theta(i)));
            set(sheet,1+i,1,rts(theta(i)));
            for (int j=1;j<elements().length;j++) {
                setformula(sheet,i+1,j+1,"sin(angles!"+rel(i+1,j+1)+")");
            }
        }
    }

    private void createChirotopeSheet() {
        Sheet sheet = workbook.createSheet("chirotope");
        set(sheet,0,0,boldStyle,"Chirotope");
        set(sheet,5,0,"ε =");
        setformula(sheet,6,0,"solver!$D$19");
        sheet.setColumnWidth(4, 35*256);
        Row row = sheet.createRow((short)1);
        createCells(row, boldStyle, "I","J","K","χ(I,J,K)","","χ","> 0","= 0");
        OMasChirotope om = epl.getEquivalentOM().getChirotope();
        int r = 2;
        int lg = elements().length - 1;
        int lg1 = elements().length - 2;
        for (int i=1;i<elements().length-2;i++) {
            for (int j=i+1;j<elements().length-1;j++) {
                for (int k=j+1;k<elements().length;k++) {
                    System.err.println(i+","+j+","+k);
                    row = sheet.createRow(r);
                    row.createCell(0).setCellFormula("IF(B" + r +"<"+lg1+",A"+r+",A"+r +"+1)");
                    row.createCell(1).setCellFormula("IF(C" + r +"<" + lg +",B" + r+",IF(B"+r+"<"+lg1+",B"+r+"+1,A"+(r+1)+"+1))");
                    row.createCell(2).setCellFormula("IF(C" + r +"<" + lg +",C" + r+"+1,B"+(r+1)+"+1)");
                    r++;
                    int sign = om.chi(i,j,k);
                    Cell c = row.createCell(3);
                    c.setCellValue(sign);
                    if (sign!=0) {
                        c.setCellStyle(signedStyle);
                    }
                    row.createCell(4).setCellValue(rts(append(
                                      r(i),"sin(",theta(k),"−",theta(j),")−",
                                      r(j),"sin(",theta(k),"−",theta(i),")+",
                                      r(k),"sin(",theta(j),"−",theta(i),")")));
                    row.createCell(5).setCellFormula(rSineDiff(r,"ABC")+"-"+rSineDiff(r,"BAC")+"+"+rSineDiff(r,"CAB"));
                    if (sign == 0) {
                        row.createCell(6).setCellValue(1);
                        row.createCell(7).setCellFormula("$F"+r);
                    } else {
                        row.createCell(6).setCellFormula("$D"+r+"*$F"+r+"-$G$1");
                        row.createCell(7).setCellValue(0);
                    }
                }
            }
        }
        row = sheet.getRow(2);
        initIJK(row.getCell(0),1.0);
        initIJK(row.getCell(1),2.0);
        initIJK(row.getCell(2),3.0);
    }

    private String rSineDiff(int r,String abc) {
        String a = abc.substring(0,1);
        String b = abc.substring(1,2);
        String c = abc.substring(2,3);
        return "OFFSET(data!$C$1,$"+a+r+",0)*OFFSET(sines!$B$2,$"+b+r+",$"+c+r+")";
    }

    private void initIJK(Cell cc, double v) {
        cc.setCellFormula(null);
        cc.setCellValue(v);
        cc.setCellType(CellType.NUMERIC);
    }
    private Cell set(Sheet sheet, int i, int j, String content) {
        Cell c = getOrCreate(sheet,i,j);
        c.setCellValue(content);
        return c;
    }

    private void set(Sheet sheet, int i, int j, CellStyle style, String content) {
        set(sheet,i,j,content).setCellStyle(style);
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

    private String rel(int i, int j) {
        return a2z(i)+(j+1);
    }
    private String absrel(int i, int j) {
        return "$"+a2z(i)+(j+1);
    }
    private String relabs(int i, int j) {
        return a2z(i)+"$"+(j+1);
    }
    private String abs(int i, int j) {
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
    
    private String[] __append(String[] ... a) {
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

    private String[] append(Object ... a) {
        String uniform[][] = new String[a.length][];
        for (int i=0;i<a.length;i++) {
            if (a[i] instanceof String) {
                uniform[i] = new String[]{(String)a[i]};
            } else {
                uniform[i] = (String[])a[i];
            }
        }
        return __append(uniform);
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
