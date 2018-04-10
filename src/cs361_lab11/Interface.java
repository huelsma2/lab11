package cs361_lab11;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Interface extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Thread[] _threads = new Thread[8];
	private boolean _timeToKill = false;
	
	/**
	 * Sensor which can run on a thread. Upon interruption it prints when
	 * it was interrupted
	 * @author Andrew Huelsman
	 *
	 */
	private class Sensor implements Runnable
	{
		String _name;
		Time _init, _final;
		
		/**
		 * creates a sensor with a name
		 * @param n name
		 */
		public Sensor(String n)
		{
			_name=n;
			_init = new Time();
		}
		
		@Override
		public void run() {
			try {
				while(true)
					Thread.sleep(5000);
			} catch (InterruptedException e) {
				end();
				System.out.println(_name + " was interrupted at " +
				Time.difference(_init, _final).convertRawTime() + " nanoseconds");
				if(!_name.equals("Sensor7") && !_timeToKill)run();
			}
		}
		
		public void end()
		{
			_final=new Time();
		}
	}
	
	private class SensorView extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Sensor _mine;
		public void addSensor(Sensor s)
		{
			_mine = s;
			JButton egg = new JButton(s._name);
			egg.addActionListener(new MyListener());
			add(egg);
			
		}
		
		private class MyListener implements ActionListener
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				int sensor = Integer.parseInt(_mine._name.charAt(6) + "");
				_threads[sensor].interrupt();
			}
			
		}
	}
	
	/**
	 * Create a window for the thread lab
	 */
	public Interface()
	{
		setLayout(new GridLayout(4,2));
		setTitle("Look at these buttons");
		setSize(800,800);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		createComponents();
		setVisible(true);
		try {
			_threads[7].join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_timeToKill = true;
		for(Thread t : _threads)t.interrupt();
		dispose();
	}
	
	/**
	 * Adds buttons and creates respective threads
	 */
	private void createComponents()
	{
		for(int i = 0; i < 8; ++i)
		{
			SensorView egg = new SensorView();
			Sensor foot = new Sensor("Sensor" + i);
			egg.addSensor(foot);
			add(egg);
			_threads[i] = new Thread(foot);
			_threads[i].start();
		}
	}
	
	/**
	 * Creates window
	 * @param args
	 */
	public static void main(String[] args)
	{
		new Interface();
	}
	
}
