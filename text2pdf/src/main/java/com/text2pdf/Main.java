package com.text2pdf;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;

/**
 * TODO: Add Javadoc
 *
 */
public class Main {
    public static void main( String[] args ) throws FileNotFoundException {
        if (args.length == 0) {
            System.out.println("Please enter a file path.");
            return;
        }
        
        String path = args[0];
        if (args.length == 1) {
            makePDF(path, path.substring(0, path.length() - 4) + ".pdf");
        }
        else {
            String outpath = args[1];
            makePDF(path, outpath);
        }
    }

    public static void makePDF(String inpath, String outpath) throws FileNotFoundException {

        HashMap<String, Integer> commands = new HashMap<String, Integer>() {{
            put("paragraph", 0);
            put("bold", 0);
            put("italics", 0);
            put("large", 0);
            put("indent", 0);
        }};

        ArrayList<String> lines = new ArrayList<String>();

        try {
            File infile = new File(inpath);
            Scanner read = new Scanner(infile);
            while (read.hasNextLine()) {
                lines.add(read.nextLine());
            }
            read.close();
        }
        catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }

        PdfWriter writer = new PdfWriter(outpath);
        PdfDocument pdf = new PdfDocument(writer);
        pdf.addNewPage();
        Document document = new Document(pdf);

        convert(commands, lines, document);
        document.close();
    }

    public static void convert(HashMap<String, Integer> commands, ArrayList<String> lines, Document document){
        Paragraph addPara = new Paragraph("");
        Text finaltext = new Text("");

        for (String line : lines) {

            if (line.charAt(0) == '.') {

                if (finaltext != new Text("")) {
                    updatePara(commands, finaltext, addPara);
                    finaltext = new Text("");
                }

                line = line.trim();
                String command = "";
                String value = "";

                if (line.indexOf(' ') == -1) {
                    command = line.substring(1);
                }
                else {
                    command = line.substring(1, line.indexOf(' '));
                    value = line.substring(line.indexOf(' ')+2);
                }

                if (command.equals("paragraph")) {
                    if (addPara != new Paragraph("")) {
                        document.add(addPara);
                    }
                    addPara = new Paragraph("");
                }
                else if (command.equals("large")){
                    commands.put("large", 1);
                }
                else if (command.equals("bold")){
                    commands.put("bold", 1);
                }
                else if (command.equals("italics")){
                    commands.put("italics", 1);
                }
                else if (command.equals("indent")){

                    if (line.charAt(line.length()-2) == '-') {
                        commands.put("indent", commands.get("indent") - Integer.parseInt(value));
                    }
                    else {
                        commands.put("indent", commands.get("indent") + Integer.parseInt(value));
                    }
                    addPara.setMarginLeft(10*commands.get("indent"));
                }
                else if (command.equals("fill")){
                    addPara.setTextAlignment(TextAlignment.JUSTIFIED);
                }
                else if (command.equals("normal")){
                    commands.put("large", 0);
                }
                else if (command.equals("regular")){
                    commands.put("bold", 0);
                    commands.put("italics", 0);
                }
                else if (command.equals("nofill")){
                    if (addPara != new Paragraph("")) {
                        document.add(addPara);
                    }
                    addPara = new Paragraph("");
                }
                else {
                    System.out.println("Invalid command.");
                }
            }
            else {
                finaltext = new Text(finaltext.getText() + line);
            }
        }
        if (finaltext != new Text("")) {
            updatePara(commands, finaltext, addPara);
        }
        if (addPara != new Paragraph("")) {
            document.add(addPara);
        }
    }

    public static void updatePara(HashMap<String, Integer> commands, Text finaltext, Paragraph addPara) {

        for(String command : commands.keySet()) {
            if (command.equals("bold")) {
                if (commands.get("bold") == 1) {
                    finaltext = finaltext.setBold();
                }
            }
            else if (command.equals("italics")) {
                if (commands.get("italics") == 1) {
                    finaltext = finaltext.setItalic();
                }
            }
            else if (command.equals("large")) {
                if (commands.get("large") == 1) {
                    finaltext = finaltext.setFontSize(20);
                }
            }
        }
        addPara.add(finaltext);
    }
}