package game;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Window extends BaseWindow{

    private final int size;
    private final Model model;
    private final JLabel label;
    private final MainWindow mainWindow;
    private int click = 0;
    private JButton prev;
    
    public Window(int size, MainWindow mainWindow) {
        this.size = size;
        this.mainWindow = mainWindow;
        mainWindow.getWindowList().add(this);
        model = new Model(size);

        JPanel top = new JPanel();
        
        label = new JLabel();
        updateLabelText();
        
        JButton newGameButton = new JButton();
        newGameButton.setText("New game");
        newGameButton.addActionListener(e -> newGame());
        
        top.add(label);
        top.add(newGameButton);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(size, size));

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                addButton(mainPanel, i, j);
            }
        }

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    private void addButton(JPanel panel, final int i,
            final int j) {
        final JButton button = new JButton();
        if(i == j && j == size/2) {
            button.setText("FUGITIVE");
        } else if(i == 0 && j == 0 || i == 0 && j == size-1 || i == size-1 && j == 0 || i == size-1 && j == size-1){
            button.setText("HUNTER");
        }
        button.addActionListener(e -> {
            if(click == 0){
                prev = button;
                if(model.check(i, j)){
                    return;
                }
                if(model.selectPlayer(i, j)){
                    click = 1;
                }
            }else{
                if(!model.step(i, j)){
                    click = 0;
                    return;
                }else {
                    prev.setText("");
                    button.setText(model.getActualPlayer().equals(Player.HUNTER) ? "FUGITIVE" : "HUNTER");
                    click = 0;
                }
            }
            updateLabelText();
            
            Player winner = model.findWinner();
            if (winner != Player.NOBODY) {
                showGameOverMessage(winner);
            }
        });

        panel.add(button);
    }

    private void showGameOverMessage(Player winner) {
        JOptionPane.showMessageDialog(this,
                "Game is over. Winner: " + winner.name());
        newGame();
    }
    
    private void newGame() {
        Window newWindow = new Window(size, mainWindow);
        newWindow.setVisible(true);
        
        this.dispose();
        mainWindow.getWindowList().remove(this);
    }
    
    private void updateLabelText() {
        label.setText("Current player: "
                + model.getActualPlayer().name()+ 
                "   Remaining moves: "+ model.moves);
    }

    @Override
    protected void doUponExit() {
        super.doUponExit();
        mainWindow.getWindowList().remove(this);
    }
    
}
