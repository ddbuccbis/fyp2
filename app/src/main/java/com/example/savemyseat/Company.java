package com.example.savemyseat;

public class Company {

        private int compID;
        private String compName;
        private String location;
        private int rating;
        private String compType;
        private int compPhone;


        public Company(int compID, String compName, String location, int rating, String compType, int compPhone) {
            this.setCompID(compID);
            this.setCompName(compName);
            this.setLocation(location);
            this.setRating(rating);
            this.setCompType(compType);
            this.setCompPhone(compPhone);
        }


        public int getCompID() {
            return compID;
        }

        public void setCompID(int compID) {
            this.compID = compID;
        }

        public String getCompName() {
            return compName;
        }

        public void setCompName(String compName) {
            this.compName = compName;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getCompType() {
            return compType;
        }

        public void setCompType(String compType) {
            this.compType = compType;
        }

        public int getCompPhone() {
            return compPhone;
        }

        public void setCompPhone(int compPhone) {
            this.compPhone = compPhone;
        }
    }
