package it.uniba.di.easyhome;

public class Home {

        private String name;
        private String owner;




        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }


        public Home(String nome, String u) {
            this.name = nome;
            this.owner = u;



    }}
