import java.util.*;
import java.util.Comparator;

public class Elevator
{
	public static final int DIR_IDLE = 0;
	public static final int DIR_UP = 1;
	public static final int DIR_DOWN = 2;
	
	public int currentFloor = 0;
	public int nextFloor = 0;
	public int direction = 0;
	
	public Elevator(){}
	
	public void goToFloor(int floor)
	{
		if(floor > 0)
		{
			this.nextFloor = floor;
			if(nextFloor < currentFloor)
			{
				direction = DIR_DOWN;
			}
			else
			{
				direction = DIR_UP;
			}
			
			System.out.println("Going to: " + nextFloor + " from " + currentFloor);//+ floor + " From: " currentFloor + " Direction: " + direction);
			//fakefakefakefake
			try {
			  Thread.sleep(5000L);    // 5 seconds
			}
			catch (Exception e) {}
			
			//finish
			this.endAtFloor(floor);
		}
	}
	
	private void endAtFloor(int floor)
	{
		System.out.println("At floor: " + floor);
		this.currentFloor = floor;
	}
	
	//main app
	public static void main(String[] args)
	{
		Elevator e = new Elevator();
		RequestController r = new RequestController(e);
		
		System.out.println("Running");
		
		r.requestToGoUp(4);
		r.goToFloor(4,10);
		
		r.requestToGoDown(8);
		r.goToFloor(8,6);
		
		r.start();
	}
}

class RequestController
{
	private Comparator<Request> comp;
	private PriorityQueue<Request> reqs;
	private Elevator elevator;
	
	public RequestController(Elevator a_elevator)
	{
		comp = new RequestComparator();
		reqs = new PriorityQueue<Request>(1000, comp);
		elevator = a_elevator;
	}
	
	public void start()
	{
		while (reqs.size() != 0)
        {
           Request next = reqs.remove();
		   elevator.goToFloor(next.toFloor);
       	}

		try {
		  Thread.sleep(5000L);    // 5 seconds
		}
		catch (Exception e) {}
		start();
	}
	//requesting outside elevator to go up
	public void requestToGoUp(int fromFloor)
	{
		//input the request
		reqs.add(new Request(elevator.nextFloor, fromFloor, elevator));
	}
	
	//requesting outside elevator to go down
	public void requestToGoDown(int fromFloor)
	{
		//input the request
		reqs.add(new Request(elevator.nextFloor, fromFloor, elevator));
	}
	
	//requesting inside elevator to go somewhere
	public void goToFloor(int fromFloor, int toFloor)
	{
		reqs.add(new Request(fromFloor, toFloor, elevator));
	}
}

class Request
{
	public int fromFloor;
	public int toFloor;
	public Elevator elevator;
	public int direction;
	
	public Request(int a_fromFloor, int a_toFloor, Elevator a_elevator)
	{
		this.fromFloor = a_fromFloor;
		this.toFloor = a_toFloor;
		this.elevator = a_elevator;
		
		if(fromFloor < toFloor)
		{
			this.direction = Elevator.DIR_UP;
		}else
		{
			this.direction = Elevator.DIR_DOWN;
		}
	}
}

//This is asimple method for deciding priority of requests in an elevator

//prefer those that are in the range of floors currently travelling

class RequestComparator implements Comparator<Request>
{
    @Override
    public int compare(Request a, Request b)
    {
		if(a == null || b == null)
		{
			return 0;
		}
		
		if(a.elevator != b.elevator)
		{
			return 0;
		}
		
		//check current state of the elevator
		Elevator elevatorA = a.elevator;
		Elevator elevatorB = b.elevator;
		
		//both requests should be going to same elevator
		if(elevatorA != elevatorA)
		{
			return 0;
		}
		
		int difA = a.fromFloor - a.toFloor;
		int difB = b.fromFloor - b.toFloor;
		
		//elevator going up currently
		if(elevatorA.direction == Elevator.DIR_UP)
		{
			if(a.direction == Elevator.DIR_UP && (a.fromFloor <= elevatorA.nextFloor && a.fromFloor >= elevatorA.currentFloor))
			{
				return 1;
			}
			else if(b.direction == Elevator.DIR_UP && (b.fromFloor <= elevatorA.nextFloor && b.fromFloor >= elevatorA.currentFloor))
			{
				return -1;
			}
			return 0;
		}
		
		//elevator going down currently
		if(elevatorA.direction == Elevator.DIR_DOWN)
		{
			if(a.direction == Elevator.DIR_DOWN && (a.fromFloor >= elevatorA.nextFloor && a.fromFloor < elevatorA.currentFloor))
			{
				return 1;
			}
			else if(b.direction == Elevator.DIR_DOWN && (b.fromFloor >= elevatorA.nextFloor && b.fromFloor < elevatorA.currentFloor))
			{
				return -1;
			}
			return 0;
		}
		
		//elevator idle, take greedily
		if(elevatorA.direction == Elevator.DIR_IDLE)
		{
			return 1;
		}
		
        return 0;
    }
}