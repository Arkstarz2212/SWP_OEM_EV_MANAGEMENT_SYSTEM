package org.example.models.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ReportResponse {
    private String reportId;
    private String reportType;
    private String title;
    private LocalDateTime generatedAt;
    private String generatedBy;
    private String downloadUrl;
    private ReportData data;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public ReportData getData() {
        return data;
    }

    public void setData(ReportData data) {
        this.data = data;
    }

    public static class ReportData {
        private Map<String, Object> summary;
        private List<Map<String, Object>> details;
        private List<ChartData> charts;

        public Map<String, Object> getSummary() {
            return summary;
        }

        public void setSummary(Map<String, Object> summary) {
            this.summary = summary;
        }

        public List<Map<String, Object>> getDetails() {
            return details;
        }

        public void setDetails(List<Map<String, Object>> details) {
            this.details = details;
        }

        public List<ChartData> getCharts() {
            return charts;
        }

        public void setCharts(List<ChartData> charts) {
            this.charts = charts;
        }

        public static class ChartData {
            private String chartType;
            private String title;
            private List<String> labels;
            private List<DataSeries> series;

            public String getChartType() {
                return chartType;
            }

            public void setChartType(String chartType) {
                this.chartType = chartType;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<String> getLabels() {
                return labels;
            }

            public void setLabels(List<String> labels) {
                this.labels = labels;
            }

            public List<DataSeries> getSeries() {
                return series;
            }

            public void setSeries(List<DataSeries> series) {
                this.series = series;
            }

            public static class DataSeries {
                private String name;
                private List<Number> data;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<Number> getData() {
                    return data;
                }

                public void setData(List<Number> data) {
                    this.data = data;
                }
            }
        }
    }
}