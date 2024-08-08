package view.menu;

import view.containers.ButtonB;
import view.containers.PanelB;

import javax.swing.*;

import static controller.constants.DimensionConstants.LEADERBOARD_DIMENSION;
import static controller.constants.DimensionConstants.SCROLL_PANE_DIMENSION;
import static controller.constants.UIConstants.*;

public class LeaderBoard extends PanelB {
    private static LeaderBoard INSTANCE = new LeaderBoard();
    private JScrollPane scrollPane = new JScrollPane();

    private LeaderBoard() {
        super(LEADERBOARD_DIMENSION.getValue());
        setLayout(null);
        initializeScrollPane();
        initializeExitButton();
    }

    private void initializeScrollPane() {
        JViewport viewport = new JViewport();
        viewport.setBackground(SCI_FI_DARK_BLUE);
        viewport.setView(new JLabel("Leaderboard"));
        scrollPane.setColumnHeader(viewport);
        scrollPane.setBackground(SCI_FI_DARK_BLUE);
        scrollPane.setForeground(SCI_FI_BLUE);
        scrollPane.setBounds(
                (LEADERBOARD_DIMENSION.getValue().width - SCROLL_PANE_DIMENSION.getValue().width) / 2,
                0,
                SCROLL_PANE_DIMENSION.getValue().width,
                SCROLL_PANE_DIMENSION.getValue().height
        );
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setBackground(SCI_FI_DARK_BLUE);
        scrollPane.getVerticalScrollBar().setForeground(SCI_FI_BLUE);
        add(scrollPane);
    }

    private void initializeExitButton() {
        ButtonB exit = new ButtonB(ButtonB.ButtonType.SMALL_MENU_BUTTON, "OK", (int) LEADERBOARD_BUTTON_WIDTH.getValue(), false);
        exit.setBounds(
                (LEADERBOARD_DIMENSION.getValue().width - (int) LEADERBOARD_BUTTON_WIDTH.getValue()) / 2,
                LEADERBOARD_DIMENSION.getValue().height - 100,
                (int) LEADERBOARD_BUTTON_WIDTH.getValue(),
                50
        );
        exit.addActionListener(e -> {
            INSTANCE.togglePanel();
            MainMenu.flushINSTANCE();
            MainMenu.getINSTANCE().togglePanel();
        });
        add(exit);
    }

    public void setScrollPane(JScrollPane scrollPane) {
        remove(this.scrollPane);
        this.scrollPane = scrollPane;
        initializeScrollPane();
        revalidate();
        repaint();
    }

    public static LeaderBoard getINSTANCE() {
        System.out.println("LeaderBoard getINSTANCE");
        return INSTANCE;
    }
}
