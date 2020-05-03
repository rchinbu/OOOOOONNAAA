import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;

import java.awt.Color;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class gameStage {

	private JFrame frmOoona;

	private AddPlayer addPlayers = new AddPlayer();
	ArrayList <String> temp = new ArrayList<>();
	 String []pids;
	 GamePlay game;
	 ArrayList <JButton> cardButtons = new ArrayList <JButton>();
	 ArrayList<String> cardIds;
//	 Popup window;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gameStage window = new gameStage(null);
					window.frmOoona.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public gameStage(ArrayList<String> playerIds) {
		initialize();
		temp= playerIds;
		pids= temp.toArray(new String[playerIds.size()]);
		game = new GamePlay();
//		game.start(game);
	//	topCard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/PNGs/small/" + game.getTopCardImage())));
	    setButtonIcons();
	}
	
	public void setVisible(){
	    this.frmOoona.setVisible(true);
	}


	
	public void setButtonIcons()
	{
		String listString= game.getPlayerHand(game.getCurrentPlayer()).stream().map(Object::toString).collect(Collectors.joining(" "));
		String[] cardNames = listString.split(",");
		cardIds = new ArrayList<>(Arrays.asList(cardNames));
		//set the cards on player's hand to display
		for (int i=0;i<cardIds.size();i++)
		{
			cardButtons.get(i).setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/PNGs/small/" + cardIds.get(i) + ".png")));
		}
		// set the buttons that the player doesn't have to null
		for (int i=cardIds.size();i<cardButtons.size();i++)
		{
			cardButtons.get(i).setIcon(null);
			
		}
	
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmOoona =  new JFrame();
		frmOoona.setTitle("OOONA");
		frmOoona.getContentPane().setBackground(Color.WHITE);
		frmOoona.setBounds(100, 100, 450, 300);
		frmOoona.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnNewButton = new JButton("New button");
		
		JButton btnNewButton_1 = new JButton("New button");
		
		JButton btnNewButton_2 = new JButton("New button");
		
		JButton btnNewButton_3 = new JButton("New button");
		
		JButton btnNewButton_4 = new JButton("New button");
		
		JButton btnNewButton_5 = new JButton("New button");
		
		JButton btnNewButton_6 = new JButton("New button");
		
		JButton btnNewButton_7 = new JButton("New button");
		
		JButton btnNewButton_8 = new JButton("New button");
		
		JButton btnNewButton_9 = new JButton("New button");
		
		JButton btnNewButton_10 = new JButton("New button");
		
		JButton btnNewButton_11 = new JButton("New button");
		
		JButton btnNewButton_12 = new JButton("New button");
		
		JButton btnNewButton_13 = new JButton("New button");
		
		JButton btnNewButton_14 = new JButton("New button");
		
		JButton btnNewButton_15 = new JButton("New button");
		
		JButton btnNewButton_16 = new JButton("New button");
		
		JButton btnNewButton_17 = new JButton("New button");
		cardButtons.add(btnNewButton);
		cardButtons.add(btnNewButton_1);
		cardButtons.add(btnNewButton_2);
		cardButtons.add(btnNewButton_3);
		cardButtons.add(btnNewButton_4);
		cardButtons.add(btnNewButton_5);
		cardButtons.add(btnNewButton_6);
		cardButtons.add(btnNewButton_7);
		cardButtons.add(btnNewButton_8);
		cardButtons.add(btnNewButton_9);
		cardButtons.add(btnNewButton_10);
		cardButtons.add(btnNewButton_11);
		cardButtons.add(btnNewButton_12);
		cardButtons.add(btnNewButton_13);
		cardButtons.add(btnNewButton_14);
		cardButtons.add(btnNewButton_15);
		cardButtons.add(btnNewButton_16);
		cardButtons.add(btnNewButton_17);
		JLabel lblNewLabel = new JLabel("My cards");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblNewLabel.setForeground(Color.BLUE);
		
		JButton drawCardButton = new JButton("Draw Card");
		drawCardButton.setForeground(Color.BLUE);
		drawCardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JLabel message = new JLabel (game.getCurrentPlayer()+ "drew a card!");
				message.setFont(new Font("Arial",Font.BOLD,48));
				JOptionPane.showMessageDialog(null, message); 
				
//				try
//				{
//					
//				}
			}
		}

				);
		
		JButton downCard = new JButton("New button");
		downCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton topCard = new JButton("New button");
		GroupLayout groupLayout = new GroupLayout(frmOoona.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_2, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_3, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_4, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_5, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_6, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_7, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_8, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
							.addGap(258)
							.addComponent(drawCardButton, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnNewButton_9, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_10, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_11, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_12, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_13, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_14, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_15, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_16, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_17, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(downCard, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(topCard, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(downCard, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(topCard, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
					.addGap(57)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblNewLabel)
						.addComponent(drawCardButton))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton_9, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_10, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_11, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_12, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_13, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_14, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_15, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_16, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_17, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_2, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_3, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_4, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_5, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_6, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_7, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_8, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		frmOoona.getContentPane().setLayout(groupLayout);
		topCard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/PNGs/small/" + game.getTopCardImage())));
		

	}
	
	
}
