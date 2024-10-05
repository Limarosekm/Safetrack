package com.example.safetrack;

import java.util.List;

public class WeatherResponse {
    private Location location;
    private Current current;
    private Forecast forecast;

    // Getters and Setters
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    public static class Location {
        private String name;
        // Add other location fields here

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Current {
        private double temp_c;
        private Condition condition;

        public double getTempC() {
            return temp_c;
        }

        public void setTempC(double tempC) {
            this.temp_c = tempC;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }
    }

    public static class Condition {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class Forecast {
        private List<ForecastDay> forecastday;

        public List<ForecastDay> getForecastday() {
            return forecastday;
        }

        public void setForecastday(List<ForecastDay> forecastday) {
            this.forecastday = forecastday;
        }

        public static class ForecastDay {
            private String date;
            private Day day;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public Day getDay() {
                return day;
            }

            public void setDay(Day day) {
                this.day = day;
            }

            public static class Day {
                private double maxtemp_c;
                private double mintemp_c;
                private Condition condition;

                public double getMaxtempC() {
                    return maxtemp_c;
                }

                public void setMaxtempC(double maxtempC) {
                    this.maxtemp_c = maxtempC;
                }

                public double getMintempC() {
                    return mintemp_c;
                }

                public void setMintempC(double mintempC) {
                    this.mintemp_c = mintempC;
                }

                public Condition getCondition() {
                    return condition;
                }

                public void setCondition(Condition condition) {
                    this.condition = condition;
                }
            }
        }
    }
}
