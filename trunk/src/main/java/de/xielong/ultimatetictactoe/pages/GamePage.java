package de.xielong.ultimatetictactoe.pages;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import resources.ResourceLocator;
import de.xielong.ultimatetictactoe.data.Board;
import de.xielong.ultimatetictactoe.data.Field;
import de.xielong.ultimatetictactoe.data.Game;
import de.xielong.ultimatetictactoe.data.Player;
import de.xielong.wicket.behaviors.CssClassAppender;
import de.xielong.wicket.component.GenericMarkupContainer;

public class GamePage extends WebPage {

    public static class Paramaters {

        public static final String GAME_ID = "id";
    }

    private static final Map<Integer, Game> GAMES     = new HashMap<Integer, Game>();

    private static final AtomicInteger      SEQUENCER = new AtomicInteger(0);

    private Game                            currentGame;

    public GamePage(PageParameters parameters) {
        super(parameters);

        initializeCurrentGame(parameters);

        createGameComponents();
    }

    private void createGameComponents() {
        WebMarkupContainer main = new WebMarkupContainer("main");
        add(main);
        main.add(new CssClassAppender(new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                return currentGame.getWhoseTurn().getCssClass();
            }
        }));

        RepeatingView boards = new RepeatingView("boards");
        main.add(boards);
        for (Board board : currentGame.getBoards()) {
            final Model<Board> boardModel = Model.of(board);
            GenericMarkupContainer<Board> boardContainer =
                    new GenericMarkupContainer<Board>(boards.newChildId(), boardModel) {

                        @Override
                        protected void onConfigure() {
                            super.onConfigure();

                            setEnabled(getModelObject().isEnabled());
                        }
                    };
            boardContainer.add(new CssClassAppender("disabled") {

                @Override
                public boolean isEnabled(Component component) {
                    return !component.isEnabled();
                }
            });

            boardContainer.add(new CssClassAppender(new AbstractReadOnlyModel<String>() {

                @Override
                public String getObject() {
                    Player winner = boardModel.getObject().getWinner();
                    return winner == null ? null : winner.getCssClass();
                }
            }));

            boards.add(boardContainer);

            RepeatingView fields = new RepeatingView("fields");
            boardContainer.add(fields);
            for (Field field : board.getFields()) {
                GenericMarkupContainer<Field> fieldContainer = new GenericMarkupContainer<Field>(fields.newChildId());
                fields.add(fieldContainer);

                final Model<Field> fieldModel = Model.of(field);
                Link<Field> control = new Link<Field>("control", fieldModel) {

                    @Override
                    public void onClick() {
                        /*
                         * Markierung im Feld vermerken
                         */
                        Field currentField = getModelObject();
                        currentField.setOwnedBy(currentGame.getWhoseTurn());

                        /*
                         * Alle Boards, ausser dem, mit dem Field-Index deaktivieren.
                         */
                        int currentFieldIndex = currentField.getIndex();
                        boolean enableAll = currentGame.getBoards()[currentFieldIndex].isWon();
                        for (Board board : currentGame.getBoards()) {
                            board.setAllowed(enableAll || board.getIndex() == currentFieldIndex);
                        }

                        /*
                         * Check current Board for winner.
                         */
                        // http://stackoverflow.com/a/1056352
//                        Field[] otherFields = currentField.getBoard().getFields();
//                        
//                        switch (currentFieldIndex){
//                            case 1:
//                                if ( otherFields[] )
//                        }

                        /*
                         * Turn to next player
                         */
                        switch (currentGame.getWhoseTurn()) {
                            case ONE:
                                currentGame.setWhoseTurn(Player.TWO);
                                break;
                            case TWO:
                                currentGame.setWhoseTurn(Player.ONE);
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    protected void onConfigure() {
                        super.onConfigure();

                        setEnabled(getModelObject().getOwnedBy() == null);
                    }
                };
                fieldContainer.add(control);

                control.add(new CssClassAppender(new AbstractReadOnlyModel<String>() {

                    @Override
                    public String getObject() {
                        Player winner = fieldModel.getObject().getOwnedBy();
                        return winner == null ? null : winner.getCssClass();
                    }
                }));
            }
        }
    }

    private void initializeCurrentGame(PageParameters parameters) {
        if (!parameters.isEmpty()) {

            String gameId = parameters.get(Paramaters.GAME_ID).toString();
            if (gameId != null) {
                currentGame = GAMES.get(Integer.parseInt(gameId));
            }
        }

        if (currentGame == null) {
            currentGame = new Game();
            GAMES.put(SEQUENCER.getAndIncrement(), currentGame);
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        /*
         * Use ResourcesLocator to access also sibling folders of the stylesheet.
         */
        response.render(CssHeaderItem.forReference(getApplication().getSharedResources().get(
                ResourceLocator.class,
                "css/style.less",
                getLocale(),
                getStyle(),
                getVariation(),
                false)));
    }
}
