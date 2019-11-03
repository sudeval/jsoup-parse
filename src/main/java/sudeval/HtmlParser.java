package sudeval;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class HtmlParser {

    public String fixHtml(final String htmlWithBadTagFormat) {
        return Jsoup.clean(htmlWithBadTagFormat, Whitelist.basicWithImages());
    }

    public String fixHtmlRemovingStyleTag(final String htmlWithBadStyleTag) {
        return Jsoup.clean(htmlWithBadStyleTag, Whitelist.relaxed());
    }

    public String fixHtmlKeepingOnlyStyleAttributeFromSpanTag(final String htmlWithBadStyleTag) {
        return Jsoup.clean(htmlWithBadStyleTag, Whitelist.relaxed().addAttributes("span", "style"));
    }

    public boolean isValid(String html) {
        return Jsoup.isValid(html, Whitelist.relaxed());
    }
    
}