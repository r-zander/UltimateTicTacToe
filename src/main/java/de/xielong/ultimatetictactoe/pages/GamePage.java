package de.xielong.ultimatetictactoe.pages;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.time.Duration;

import resources.ResourceLocator;
import de.xielong.ultimatetictactoe.GameRules;
import de.xielong.ultimatetictactoe.data.Board;
import de.xielong.ultimatetictactoe.data.Field;
import de.xielong.ultimatetictactoe.data.Game;
import de.xielong.ultimatetictactoe.data.OwnedState;
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

        add(new Image("explanation", new PackageResourceReference(ResourceLocator.class, "img/explanation.jpg")));

        createGameComponents();

    }

    private void createGameComponents() {
        WebMarkupContainer main = new WebMarkupContainer("main");
        addOrReplace(main);
        main.add(new CssClassAppender(new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                return currentGame.getWhoseTurn().getCssClass();
            }
        }));

        main.setOutputMarkupId(true);
        main.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));

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
                    Player winner = boardModel.getObject().getOwnedBy();
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
                        GameRules.makeMove(currentGame, getModelObject());
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
        createGameStateLine();
    }

    private void createGameStateLine() {
        WebMarkupContainer gameStateContainer = new WebMarkupContainer("gameState") {

            @Override
            protected void onConfigure() {
                super.onConfigure();

                setVisible(currentGame.getState() != OwnedState.OPEN);
            }
        };
        addOrReplace(gameStateContainer);

        gameStateContainer.add(new WebMarkupContainer("tie") {

            @Override
            protected void onConfigure() {
                super.onConfigure();

                setVisible(currentGame.getState() == OwnedState.TIED);
            }
        });

        gameStateContainer.setOutputMarkupId(true);
        gameStateContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));

        WebMarkupContainer winnerContainer = new WebMarkupContainer("winner") {

            @Override
            protected void onConfigure() {
                super.onConfigure();

                setVisible(currentGame.getState() == OwnedState.WON);
            }
        };
        gameStateContainer.add(winnerContainer);

        winnerContainer.add(new Label("whoseTurn", new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                switch (currentGame.getWhoseTurn()) {
                    case ONE:
                        return "Player 1";
                    case TWO:
                        return "Player 2";
                    default:
                        throw new IllegalStateException();
                }
            }
        }));

        gameStateContainer.add(new Link<Void>("restart") {

            @Override
            public void onClick() {
                currentGame = new Game();
                createGameComponents();
            }

        });
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
            int newId = SEQUENCER.getAndIncrement();
            GAMES.put(newId, currentGame);
            throw new RestartResponseException(GamePage.class, new PageParameters().add(Paramaters.GAME_ID, newId));
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
