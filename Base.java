public class Base { 
  public static int people_on_base = 50; 
  public static int vehicles_on_base = 30; 
  public static double petrol_on_base = 1500; 
  public static double goods_on_base = 5; 
  
  public static void main (String args[]){
    Base.petrol_on_base = 1000;
Base.people_on_base = 10;
Base.goods_on_base  = 20.000;
Base.vehicles_on_base = 10;

// polymorphism!
IVehicle v = new Expeditor(3, 5, 3.0, 5.0, 200, 300);
v.arrive();

assert(Base.people_on_base == 14);   // 10 + 3 + driver
assert(Base.goods_on_base == 23.0);  // 20 + 3
assert(Base.vehicles_on_base == 11); // 10 + 1 

v.leave();

assert(Base.people_on_base == 14 - 6);       // 5+driver left
assert(Base.goods_on_base == 23.0 - 5.0);    // 5 tons left
assert(Base.petrol_on_base == 900);          // minus (300-200)
assert(Base.vehicles_on_base == 10);

// additional: test cast to Bus and Truck
IBus b = (IBus)v;
ITruck t = (ITruck)v;
assert(b != null);
assert(t != null);

System.out.println("OK");
  }
}
  
 class Vehicle implements IVehicle {
    protected double petrol_amount = 20;
    protected double tank_volume = 21;
    
    Vehicle(double petrol_amount, double tank_volume) {
      assert ((petrol_amount>=0) && (tank_volume>=0) && (petrol_amount<=tank_volume));
      this.petrol_amount = petrol_amount;
      this.tank_volume = tank_volume;
    }
    
    public double getTankVolume() {
      return tank_volume;
    }
    public double getPetrolAmount() {
      return petrol_amount;
    }
    
    public void arrive() {
      assert((Base.people_on_base>=0) && (Base.petrol_on_base>=0) && (Base.people_on_base>=0) &&(Base.goods_on_base>=0));
      Base.vehicles_on_base++;
      Base.people_on_base++;
    }
    
    public boolean leave() {      
      if (Base.petrol_on_base >= tank_volume - petrol_amount && Base.people_on_base>0){
        Base.petrol_on_base = Base.petrol_on_base - tank_volume + petrol_amount;
        Base.vehicles_on_base --;
        this.petrol_amount=tank_volume;
        Base.people_on_base--;
        return true;}
      else{
        return false;
      }
    }
  }
  
  class Bus  extends Vehicle implements IBus { 
       Buspart b= new Buspart(0,0);
       
      Bus (int people, int max_people, double petrol, double max_petrol) {
        super(petrol,max_petrol);
        //assert((people>=0) && (petrol>0) && (people<=max_people) );
        b.people=people;
        b.max_people=max_people;
       // petrol=petrol_amount;
        //max_petrol=tank_volume;
      }
      
      public int getPeopleCount() {
        return b.people; 
      }
      
      public int getMaxPeople() {
        return b.max_people;
      }
      
      public void arrive() {
        super.arrive();
        b.arrive();
      }
      
      public boolean leave() {
        if ( (super.leave()) &&(b.leave()) ){
          return true;
        } else return false;
      }
  }
    
  class Buspart{
     public int people;
     public int max_people;
       
      Buspart (int people, int max_people) {
        this.people=people;
        this.max_people=max_people;
      }
      
      public int getPeopleCount() {
        return people; 
      }
      
      public int getMaxPeople() {
        return max_people;
      }
      
      public void arrive() {
       // super.arrive();
        Base.people_on_base=Base.people_on_base +people; 
        this.people=0;
      }
      
      public boolean leave() {
          if ( max_people<Base.people_on_base){
          Base.people_on_base=Base.people_on_base - max_people;
          people=max_people;     
        }
         if (max_people>Base.people_on_base && Base.people_on_base>0){
          people=Base.people_on_base;
          Base.people_on_base=0;
        }
        return true;
      }
  }
   
    class Truck extends Vehicle implements ITruck {
      Truckpart b=new Truckpart(0,0);
      
      private double load = 20;
      private double max_load = 30;
      
      Truck(double load, double max_load, double petrol, double max_petrol) {
        super(petrol,max_petrol);
        b.load = load;
        b.max_load=max_load;
        petrol=petrol_amount;
        max_petrol=tank_volume;////////////////////////////
      }
      
      public double getCurrentLoad() {
        return b.load;
      }
      
      public double getMaxLoad() {
        return b.max_load;
      }
      
      public void arrive() { 
        super.arrive();
       // Base.goods_on_base = Base.goods_on_base+load;
        b.arrive();
      }
      
      public boolean leave() {
          if (super.leave()==true ){
          if (Base.goods_on_base>=max_load){
            load=max_load;
            Base.goods_on_base=Base.goods_on_base - max_load;
          }
          else {
            max_load=Base.goods_on_base;
            load=max_load;
            Base.goods_on_base=0;
          }
          return true;}
        else{
          return false;
        }
      }}
    
    class Truckpart {
      public double load = 20;
      public double max_load = 30;
      
      Truckpart(double load, double max_load) {
        this.load = load;
        this.max_load=max_load;
      }
      
      public double getCurrentLoad() {
        return load;
      }
      
      public double getMaxLoad() {
        return max_load;
      }
      
      public void arrive() { 
        Base.goods_on_base = Base.goods_on_base+load;
             }
      
      public boolean leave() {
          if (Base.goods_on_base>=max_load){
            load=max_load;
            Base.goods_on_base=Base.goods_on_base - max_load;
          }
          else {
            max_load=Base.goods_on_base;
            load=max_load;
            Base.goods_on_base=0;
          }
          return true;}
    }
    
   interface IVehicle {
      public double getTankVolume();
       public double getPetrolAmount();
       public void arrive();
       public boolean leave();
    }
    interface IBus {
       public int getPeopleCount();
        public int getMaxPeople(); 
        public void arrive();
       public boolean leave();
    }
    interface ITruck {
       public double getCurrentLoad();
      public double getMaxLoad();
      public void arrive();
       public boolean leave();
    }
    
    interface  IExpeditor{
     public int getPeopleCount();
     public int getMaxPeople();
    public double getCurrentLoad();
      public double getMaxLoad();
      public void arrive();
      public boolean leave();
    }
     
class Expeditor extends Vehicle implements IExpeditor { 
 
 Buspart a = new Buspart(0,0);
 Truckpart b = new Truckpart(0,0);
 
 public Expeditor(int people, int max_people, double load, double max_load, double petrol, double max_petrol) {
  super(petrol,max_petrol);
  a.people = people;
  a.max_people = max_people;  
  b.load = load;
  b.max_load = max_load;  
 }
 
 public int getPeopleCount() {
  return a.people;  
 }
 
 public int getMaxPeople() {
  return a.max_people;  
 }
 
 public double getCurrentLoad() {
  return b.load;
 }
 
 public double getMaxLoad() {
  return b.max_load; 
 }
 
 public void arrive() {
  super.arrive();  
  a.arrive();
  b.arrive();  
 }
 
 public boolean leave(){
  if ( (super.leave()) && (a.leave()) && (b.leave()) ){ 
   return true;     
   } else return false; 
 }
 }

    