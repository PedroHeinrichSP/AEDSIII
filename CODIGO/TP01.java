import java.util.Date;

class TP01{
    public static class Movie{
        //Atributos
        String title; // Título do filme   
        Date year; // Data padrão, definir e usar somente o ano
        String certificate; // Unrated, (Banned), U, UA, G, A, PG-13, 12+, 15+, 16+, 18+, 7+, All, Approved, GP, M/PG, NC-17, Not Rated, PG, R, 7, 12, 13, 16, 18, U/A, UA 13+, UA 16+, UA 7+
        int runtime; // Inteiro em minutos
        String[] genre; // Multiplas linhas para gênero
        float rating; // Média das notas de 0 a 10 do público (até uma casa decimal)
        int metascore; // Média das notas de 0 a 100 de críticos
        String synopsis; // Avaliar o uso
        String director; // Nome do diretor
        int votes; // Número de votos que o filme teve
        float gross; // Renda do filme (em milhões, duas casas decimais)
        String[] cast; // Nome dos principais atores (até 4 pessoas)

        //Construtores

        public Movie(){
            this.title = "";
            this.year = new Date();
            this.certificate = "";
            this.runtime = 0;
            this.genre = new String[0];
            this.rating = 0;
            this.metascore = 0;
            this.synopsis = "";
            this.director = "";
            this.votes = 0;
            this.gross = 0;
            this.cast = new String[0];
        }

        public Movie(String title, Date year, String certificate, int runtime, String[] genre, float rating, int metascore, String synopsis, String director, int votes, float gross, String[] cast){
            this.title = title;
            this.year = year;
            this.certificate = certificate;
            this.runtime = runtime;
            this.genre = genre;
            this.rating = rating;
            this.metascore = metascore;
            this.synopsis = synopsis;
            this.director = director;
            this.votes = votes;
            this.gross = gross;
            this.cast = cast;
        }



        //Getters
        public String getTitle(){
            return this.title;
        }
        public Date getYear(){
            return this.year;
        }
        public String getCertificate(){
            return this.certificate;
        }
        public int getRuntime(){
            return this.runtime;
        }
        public String[] getGenre(){
            return this.genre;
        }
        public float getRating(){
            return this.rating;
        }
        public int getMetascore(){
            return this.metascore;
        }
        public String getSynopsis(){
            return this.synopsis;
        }
        public String getDirector(){
            return this.director;
        }
        public int getVotes(){
            return this.votes;
        }
        public float getGross(){
            return this.gross;
        }
        public String[] getCast(){
            return this.cast;
        }

        //Setters
        public void setTitle(String title){
            this.title = title;
        }
        public void setYear(Date year){
            this.year = year;
        }
        public void setCertificate(String certificate){
            this.certificate = certificate;
        }
        public void setRuntime(int runtime){
            this.runtime = runtime;
        }
        public void setGenre(String[] genre){
            this.genre = genre;
        }
        public void setRating(float rating){
            this.rating = rating;
        }
        public void setMetascore(int metascore){
            this.metascore = metascore;
        }
        public void setSynopsis(String synopsis){
            this.synopsis = synopsis;
        }
        public void setDirector(String director){
            this.director = director;
        }
        public void setVotes(int votes){
            this.votes = votes;
        }
        public void setGross(float gross){
            this.gross = gross;
        }
        public void setCast(String[] cast){
            this.cast = cast;
        }

        //Métodos
        public void print(){
            System.out.println("Título: " + this.title);
            System.out.println("Ano: " + this.year);
            System.out.println("Certificado: " + this.certificate);
            System.out.println("Duração: " + this.runtime);
            System.out.println("Gênero: " + this.genre);
            System.out.println("Nota: " + this.rating);
            System.out.println("Metascore: " + this.metascore);
            System.out.println("Sinopse: " + this.synopsis);
            System.out.println("Diretor: " + this.director);
            System.out.println("Votos: " + this.votes);
            System.out.println("Renda: " + this.gross);
            System.out.println("Elenco: " + this.cast);
        }

        public Movie clone(){
            Movie movie;
            movie = new Movie(this.title, this.year, this.certificate, this.runtime, this.genre, this.rating, this.metascore, this.synopsis, this.director, this.votes, this.gross, this.cast);
            return movie;
        }
        
        public boolean equals(Movie movie){
            boolean equals = false;
            if(this.title.equals(movie.title) && this.year.equals(movie.year) && this.certificate.equals(movie.certificate) && this.runtime == movie.runtime && this.genre.equals(movie.genre) && this.rating == movie.rating && this.metascore == movie.metascore && this.synopsis.equals(movie.synopsis) && this.director.equals(movie.director) && this.votes == movie.votes && this.gross == movie.gross && this.cast.equals(movie.cast)){
                equals=true;
            } else equals=false;
            return equals;
        }

        public String toString(){
            return "Título: " + this.title + "\nAno: " + this.year + "\nCertificado: " + this.certificate + "\nDuração: " + this.runtime + "\nGênero: " + this.genre + "\nNota: " + this.rating + "\nMetascore: " + this.metascore + "\nSinopse: " + this.synopsis + "\nDiretor: " + this.director + "\nVotos: " + this.votes + "\nRenda: " + this.gross + "\nElenco: " + this.cast;
        }

        public int compareToYear(Movie movie){
            int compare = 0;
            if(this.year.compareTo(movie.year) > 0){
                compare = 1;
            } else if(this.year.compareTo(movie.year) < 0){
                compare = -1;
            } else if(this.year.compareTo(movie.year) == 0){
                compare = 0;
            }
            return compare;
        }

        public int compareToRuntime(Movie movie){
            int compare = 0;
            if(this.runtime > movie.runtime){
                compare = 1;
            } else if(this.runtime < movie.runtime){
                compare = -1;
            } else if(this.runtime == movie.runtime){
                compare = 0;
            }
            return compare;
        }

        public int compareToRating(Movie movie){
            int compare = 0;
            if(this.rating > movie.rating){
                compare = 1;
            } else if(this.rating < movie.rating){
                compare = -1;
            } else if(this.rating == movie.rating){
                compare = 0;
            }
            return compare;
        }

        public int compareToMetascore(Movie movie){
            int compare = 0;
            if(this.metascore > movie.metascore){
                compare = 1;
            } else if(this.metascore < movie.metascore){
                compare = -1;
            } else if(this.metascore == movie.metascore){
                compare = 0;
            }
            return compare;
        }

        public int compareToVotes(Movie movie){
            int compare = 0;
            if(this.votes > movie.votes){
                compare = 1;
            } else if(this.votes < movie.votes){
                compare = -1;
            } else if(this.votes == movie.votes){
                compare = 0;
            }
            return compare;
        }

        public int compareToGross(Movie movie){
            int compare = 0;
            if(this.gross > movie.gross){
                compare = 1;
            } else if(this.gross < movie.gross){
                compare = -1;
            } else if(this.gross == movie.gross){
                compare = 0;
            }
            return compare;
        }

        

    }
    public static void main(String[] args){
        
    }
}