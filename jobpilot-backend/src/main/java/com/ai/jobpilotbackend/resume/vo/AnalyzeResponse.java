package com.ai.jobpilotbackend.resume.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wzs
 * @date 2026年05月05日 0:00
 */
@Data
public class AnalyzeResponse {

    private Integer score;
    private String level;
    private String summary;
    private List<DimensionItem> dimensions;
    private List<DimensionItem> radar;
    private SkillsInfo skills;
    private List<SuggestionItem> suggestions;
    private List<String> advantages;
    private List<String> disadvantages;

    @Data
    public static class DimensionItem {
        private String name;
        private Integer score;
    }

    @Data
    public static class SkillsInfo {
        private List<String> matched;
        private List<String> partial;
        private List<String> missing;
    }

    @Data
    public static class SuggestionItem {
        private String type;
        private String content;
    }
}
