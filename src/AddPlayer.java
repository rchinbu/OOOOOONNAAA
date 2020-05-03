import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class AddPlayer {

	private JFrame frame;
	private JTextField playerID;
	public ArrayList<String> playerIds;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddPlayer window = new AddPlayer();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AddPlayer() {
		initialize();
		playerIds = new ArrayList<>();
	}
	public String[] getPids()
	{
		String pids[] = playerIds.toArray(new String [playerIds.size()]);
		return pids;
	}
	public void setVisible(){
	    this.frame.setVisible(true);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("New Game");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		lblNewLabel.setForeground(Color.RED);
		
		JLabel lblNewLabel_1 = new JLabel("Enter the name of the player:");
		lblNewLabel_1.setForeground(Color.RED);
		
		playerID = new JTextField();
		playerID.setForeground(Color.ORANGE);
		playerID.setColumns(10);
		
		JButton btnNewButton = new JButton("SAVE");
		JLabel playerID1 = new JLabel("");
		playerID1.setForeground(Color.ORANGE);
		
		JLabel playerID2 = new JLabel("");
		playerID2.setForeground(Color.ORANGE);
		
		JLabel playerID3 = new JLabel("");
		playerID3.setForeground(Color.ORANGE);
		
		JLabel playerID4 = new JLabel("");
		playerID4.setForeground(Color.ORANGE);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (playerID.getText().isEmpty())
				{
					JLabel message = new JLabel ("Please enter a name!");
					message.setFont(new Font("Arial",Font.BOLD,48));
					JOptionPane.showMessageDialog(null, message);
					
				}
				else
				{
					String name = playerID.getText() .trim();
					playerIds.add(name);
					
					if ( playerIds.size()== 1)
					{
						playerID1.setText(playerIds.get(0));
					}
					else if (playerIds.size()== 2)
					{
						playerID1.setText(playerIds.get(0));
						playerID2.setText(playerIds.get(1));
					}
					else if (playerIds.size()== 3)
					{
						playerID1.setText(playerIds.get(0));
						playerID2.setText(playerIds.get(1));
						playerID3.setText(playerIds.get(2));
					}
					else if (playerIds.size()== 4)
					{
						playerID1.setText(playerIds.get(0));
						playerID2.setText(playerIds.get(1));
						playerID3.setText(playerIds.get(2));
						playerID4.setText(playerIds.get(3));
					}
					
					if(playerIds.size() > 0 && playerIds.size()<5)
					{
						JLabel message = new JLabel ("Successful save!");
						message.setFont(new Font("Arial",Font.BOLD,48));
						JOptionPane.showMessageDialog(null, message);
						playerID.setText("");
					}
					
					if(playerIds.size()==5)
					{
						playerIds.remove(name);
						JLabel message = new JLabel ("There can only be 2-4 players!");
						message.setFont(new Font("Arial",Font.BOLD,48));
						JOptionPane.showMessageDialog(null, message);
					}
						
				}
			}
		});
		btnNewButton.setForeground(Color.RED);
		
		JButton btnNewButton_1 = new JButton("DONE");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (playerIds.size()<=1)
				{
					JLabel message = new JLabel ("There must be at least 2 players!");
					message.setFont(new Font("Arial",Font.BOLD,48));
					JOptionPane.showMessageDialog(null, message);
				}
				else
				{
					frame.dispose();
					new gameStage(playerIds).setVisible();
				}
			}
		});
		btnNewButton_1.setForeground(Color.RED);
		
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
					.addGap(66)
					.addComponent(playerID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(35, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(65)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(playerID2)
						.addComponent(playerID1))
					.addContainerGap(324, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGap(78)
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.RELATED, 108, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(playerID4)
						.addComponent(playerID3)
						.addComponent(btnNewButton_1))
					.addGap(72))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
						.addComponent(playerID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(playerID1)
							.addGap(47)
							.addComponent(playerID2))
						.addComponent(playerID3))
					.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
					.addComponent(playerID4)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton)
						.addComponent(btnNewButton_1))
					.addContainerGap())
		);
		frame.getContentPane().setLayout(groupLayout);
	}

}
