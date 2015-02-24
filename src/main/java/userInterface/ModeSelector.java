package main.java.userInterface;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class ModeSelector extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField txtParticipantNumber;
	int participantNumber = 0;


    private ButtonGroup buttonGroup = new ButtonGroup();
    private JRadioButton t1;
    private JRadioButton t2;
    private JPanel buttonPanel;

	public ModeSelector() {
		txtParticipantNumber = new JTextField();
		txtParticipantNumber.setText("Participant Number");
		getContentPane().add(txtParticipantNumber, BorderLayout.NORTH);
		txtParticipantNumber.setColumns(10);
		
		JButton btnTrain = new JButton("Train");
		getContentPane().add(btnTrain, BorderLayout.WEST);
		btnTrain.addActionListener(this);
		
		JButton btnTest1 = new JButton("Test Leap & Voice");
		getContentPane().add(btnTest1, BorderLayout.CENTER);
		btnTest1.addActionListener(this);
		
		JButton btnTest2 = new JButton("Test Mouse");
		getContentPane().add(btnTest2, BorderLayout.EAST);
		btnTest2.addActionListener(this);

        this.buttonPanel = new JPanel();

        this.t1 =  new JRadioButton("Task 1");
        this.t1.setSelected(true);
        this.t1.setActionCommand("task1");


        this.t2 =  new JRadioButton("Task 2");
        this.t2.setActionCommand("task2");

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));

        buttonGroup.add(t1);
        buttonGroup.add(t2);

        buttonPanel.add(t1);
        buttonPanel.add(t2);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    setVisible(true);
	    pack();
	    this.setLocation(700, 350);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try{
			participantNumber = Integer.parseInt(txtParticipantNumber.getText());
		}catch(Exception ex){
			
		}

        String taskCommand = this.buttonGroup.getSelection().getActionCommand();
        System.out.println(taskCommand);

        if(e.getActionCommand().equalsIgnoreCase("Train"))
			App.modeSelected(participantNumber, App.TestMode.Train, taskCommand);
		else if(e.getActionCommand().equalsIgnoreCase("Test Mouse"))
			App.modeSelected(participantNumber, App.TestMode.TestMouse, taskCommand);
		else
			App.modeSelected(participantNumber, App.TestMode.TestLeap, taskCommand);
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
