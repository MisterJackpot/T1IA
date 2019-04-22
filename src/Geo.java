public class Geo {
   public int posX;
   public int posY;
   public int visitas;

   public Geo(){
      this.visitas = 0;
   }

   public boolean itsEqual(int posX, int posY){
      if(this.posX == posX && this.posY == posY) return true;
      else return false;
   }
}
