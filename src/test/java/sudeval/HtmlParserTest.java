package sudeval;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * HtmlParserTest
 */
public class HtmlParserTest {

    private static final String INVALID_STYLE = "style=\"font-weigth: bold; box-sizing: border-box; font-family: OpenSansRegular, Arial, \" helvetica=\"\" neue\",=\"\" helvetica,=\"\" sans-serif;=\"\" font-size:=\"\" 11px;=\"\" background-color:=\"\" rgb(255,=\"\" 255,=\"\" 255);\"=\"\"";
    private static final String VALID_STYLE = "style=\"font-weigth: bold; box-sizing: border-box; font-family: OpenSansRegular, Arial, \"";

    private HtmlParser htmlParser;

    @Before
    public void setUp() {
        htmlParser = new HtmlParser();
    }

    @Test
    public void mustFixHtmlWithTagNotClosed() {
        String htmlWithTagNotClosed = "<span>html is open";
        String fixedHtml = htmlParser.fixHtml(htmlWithTagNotClosed);
        Assert.assertNotNull("new HTML should not be null", fixedHtml);
        Assert.assertEquals("span tag should be fixed", "<span>html is open</span>", fixedHtml);
    }

    @Test
    public void mustFixHtmlWithStyleTagProblemn() {
        String htmlWithStyleTagProblemn = "<div " + INVALID_STYLE + " >Inner text</div>";
        String fixedHtml = htmlParser.fixHtmlRemovingStyleTag(htmlWithStyleTagProblemn);
        final StringBuilder contentExpected = new StringBuilder("<div>");
        contentExpected.append("\n");
        contentExpected.append(" Inner text");
        contentExpected.append("\n");
        contentExpected.append("</div>");

        Assert.assertEquals("must be returned the indented div tag", contentExpected.toString(), fixedHtml);
    }

    @Test
    public void mustKeepOnlyStyleFontWeight() {
        String htmlWithStyleTagProblemn = "<div " + INVALID_STYLE + " ><span " + INVALID_STYLE + " >Inner text</span></div>";
        String fixedHtml = htmlParser.fixHtmlKeepingOnlyStyleAttributeFromSpanTag(htmlWithStyleTagProblemn);

        final StringBuilder contentExpected = new StringBuilder("<div>");
        contentExpected.append("\n");
        contentExpected.append(" <span ");
        contentExpected.append(VALID_STYLE + ">");
        contentExpected.append("Inner text");
        contentExpected.append("</span>");
        contentExpected.append("\n");
        contentExpected.append("</div>");

        Assert.assertEquals("must be returned only span tag with style attribute", contentExpected.toString(), fixedHtml);
    }

    private String getFileText(String completeFilePath) throws IOException {
        Path path = Paths.get(completeFilePath);

        Stream<String> lines = Files.lines(path);
        String data = lines.collect(Collectors.joining("\n"));
        lines.close();

        return data;
    }
}