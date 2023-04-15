package ai.worlds.vacuum;

import java.util.Vector;
import java.util.Arrays;

import ai.utils.Address;
import ai.utils.Consts;

/**
 * @author Vinícius Linhares -- vinicius.linhares@aluno.uece.br
 *
 * O modelo é baseado em robos aspiradores comumente usados nas casas.
 * Tem como objetivo limpar o máximo de espaços desconhecidos e depois voltar para casa e então desligar.
 *
 * A solução que proponho é a de que o agente
 * 1. sempre terá sua área de limpeza como sendo o primeiro quadrante de um plano cartesiano (x, y >= 0).
 * 2. tende a ir em direção a espaços desconhecidos.
 * 3. caso não tenha nenhum espaço desconhecido em suas proximidades, agirá aleatoriamente até determinada quantidade de ações.
 * 4. se alcançar uma determinada quantidade de ações aleatorias, irá, imediatamente traçar planos que o levem diretamente para casa e então se desligará.
 */

public class ViniVacuumAgent extends VacuumAgent {
    // no modelo é necessário voltar para casa, então deve saber qual o endereço inicial.
    private Address homeSpace = new Address(2, 2);
    // relativo a casa, seu endereço atual
    private Address currentSpace = new Address(homeSpace.getX(), homeSpace.getY());
    // em casos de correção é necessário saber qual foi o espaço do qual veio.
    private Address previousSpace = new Address(homeSpace.getX(), homeSpace.getY());
    // como é baseado em interações, deve saber se já tem um plano ou não. (talvez remover isso e só verificar o "nextAction").
    private boolean hasActionToDo;
    // toda vez que age aleatoriamente usa esse contado para registrar quantas vezes fez isso.
    // após uma determinada quantidade de ações aleatorias serem realizadas, deve voltar para casa e desligar.
    private int enoughOfRandom;
    // como o movimento dele é rotacional usara um sitema relativo de orientação para saber onde seria sua frente.
    private String[] possybleDirections = {"N","E","S","W"};
    // direção inicial.
    private String direction = possybleDirections[1];
    // lista com a coordenada da proxima ação.
    private Address targetSpace = new Address(-1, -1);
    // mapa de memoria.
    private int[][] memorizedSpace;

    // params
    private int memorizedSpaceMaxSize = 12;
    // setar aqui params com a quant max de random actions

    public ViniVacuumAgent() {
        // idealmente o mapa de memoria começar com poucas posições e, ao decorrer da execução, ir ganhando mais espaço,
        // mas pra agilizar a entrega e focando no aprendizado em IA, estou optando por ja definir um tamanho máximo do mapa.
        memorizedSpace = new int[memorizedSpaceMaxSize][memorizedSpaceMaxSize];
        for (int[] row: memorizedSpace) {
            for (@SuppressWarnings("unused") int collum: row) {
                collum = Consts.spaceState.UNKNOWN.getValue();
            }
        }
    }

    public void determineAction() {
        Vector < ? > p = (Vector < ? > ) percept;

        // se alcançou o limite de ações aleatorias, deve ir para casa casa e desligar;
        // se não tiver um plano deve criar um.
        if (enoughOfRandom >= 10) goHomeAndShutOff();
        else if (hasActionToDo == false) decideNextAction();

        // se o espaço atual estiver sujo, limpará;
        // se tiver batido em uma parede, lidará com o fato;
        // se tiver alcançado o limite de ações aleatórias, verifica se está em casa e então se desliga;
        // se tiver um plano irá segui-lo;
        // se não ele agirá de forma aleatória.
        if (p.elementAt(1) == "dirt") action = "suck";
        else if (p.elementAt(0) == "bump") thatsAWall();
        else if (shouldShutOffAtHome()) action = "shut-off";
        else if (hasActionToDo == true) {
            // verifica qual o espaço a sua frente baseado na direção que estiver posicionado.
            var fowardSpaceX = currentSpace.getX();
            var fowardSpaceY = currentSpace.getY();
            switch (direction) {
                case "N": fowardSpaceY++; break;
                case "E": fowardSpaceX++; break;
                case "S": fowardSpaceY--; break;
                case "W": fowardSpaceX--; break;
                default:;
            }

            // se o espaço a sua frente for o alvo do plano, vai para frente;
            // se não ele irá rodar para a esquerda até estar de frente para o espaço alvo.
            if ((fowardSpaceX == targetSpace.getX()) && (fowardSpaceY == targetSpace.getY())) {
                doFoward();
            } else {
                doTurnLeft();
            }
        } else doRandom();

        // verifica se completou o plano
        if (currentSpace.getX() == targetSpace.getX() && currentSpace.getY() == targetSpace.getY()) {
            hasActionToDo = false;
        }
    }

    // aux methods
    private void setSpaceTo(int state) {
        memorizedSpace[currentSpace.getX()][currentSpace.getY()] = state;
    }

    private void doTurnLeft() {
        // como a movimentação é baseada em rotação, é necessário sempre saber para que direção o agente está olhando
        // como estou usando e uma lista de possibilidades tenho q fazer um "circulo" na lista
        // talvez o melhor fosse isolar em funções de "proxima direção" e "direção anterior"
        var actualDirection = Arrays.asList(possybleDirections).indexOf(direction);
        var newDirection = (actualDirection == 0) ? 3 : actualDirection - 1;
        direction = possybleDirections[newDirection];
        action = "turn left";
    }

    private void doTurnRight() {
        // como a movimentação é baseada em rotação, é necessário sempre saber para que direção o agente está olhando
        // como estou usando e uma lista de possibilidades tenho q fazer um "circulo" na lista
        // talvez o melhor fosse isolar em funções de "proxima direção" e "direção anterior"
        var actualDirection = Arrays.asList(possybleDirections).indexOf(direction);
        var newDirection = (actualDirection == 3) ? 0 : actualDirection + 1;
        direction = possybleDirections[newDirection];
        action = "turn right";
    }

    private void doFoward() {
        // registra na memoria o espaço conhecido, no caso um espaço limpo
        setSpaceTo(Consts.spaceState.CLEAN.getValue());

        // registra qual o espaço atual ANTES do movimento, no caso de precisar corrigir o mapa de memoria.
        previousSpace.setX(currentSpace.getX());
        previousSpace.setY(currentSpace.getY());

        // baseado na direção vai mudar a posição atual no mapa de memoria.
        if (direction == "N") currentSpace.setY(currentSpace.getY() + 1);
        else if (direction == "E") currentSpace.setX(currentSpace.getX() + 1);
        else if (direction == "S") currentSpace.setY(currentSpace.getY() - 1);
        else if (direction == "W") currentSpace.setX(currentSpace.getX() - 1);

        action = "forward";
    }

    private void thatsAWall() {
        // primeiro corrige o espaço no qual tentou entrar e descobriu ser uma parede.
        setSpaceTo(Consts.spaceState.OBSTACLE.getValue());
        // se tinha um plano deve aborta-lo.
        hasActionToDo = false;
        // já que bateu, então seu movimento anterior falhou, portanto deve corrigir sua posição atual no mapa de memoria.
        currentSpace.setX(previousSpace.getX());
        currentSpace.setY(previousSpace.getY());
        // o movimento padrão será virar a esquerda.
        doTurnLeft();
    }

    private void decideNextAction() {
        // lista com os possíveis endereços que o agente terá como alvo
        var possibleActions = new Address[4];
        for (var i = 0; i < 4; i++) {
            possibleActions[i] = new Address(-1, -1);
        }
        // olhará para as 4 direções no mapa de memoria e registrará, em uma lista de possieis ações, os locais desconhecidos.

        // olhar para cima
        if (memorizedSpace[currentSpace.getX()][currentSpace.getY() + 1] == Consts.spaceState.UNKNOWN.getValue()) {
            possibleActions[0].setX(currentSpace.getX());
            possibleActions[0].setY(currentSpace.getY() + 1);
        };

        // olhar para direita
        if (memorizedSpace[currentSpace.getX() + 1][currentSpace.getY()] == Consts.spaceState.UNKNOWN.getValue()) {
            possibleActions[1].setX(currentSpace.getX() + 1);
            possibleActions[1].setY(currentSpace.getY());
        };

        // olhar para baixo
        if (memorizedSpace[currentSpace.getX()][currentSpace.getY() - 1] == Consts.spaceState.UNKNOWN.getValue()) {
            possibleActions[2].setX(currentSpace.getX());
            possibleActions[2].setY(currentSpace.getY() - 1);
        };

        // olhar para esquerda
        if (memorizedSpace[currentSpace.getX() - 1][currentSpace.getY()] == Consts.spaceState.UNKNOWN.getValue()) {
            possibleActions[3].setX(currentSpace.getX() - 1);
            possibleActions[3].setY(currentSpace.getY());
        };

        // filtra a lista de possiveis ações para encontrar somente endereços válidos para executar um plano.
        // um plano válido n contem o valor default (0).
        var filteredActions = Arrays.stream(possibleActions).filter(possibleAction -> ((possibleAction.getX() > 0) && (possibleAction.getY() > 0))).toArray(Address[]::new);

        // se houver esdereços na lista filtrada, escolhe, aleatoriamente, um destes como endereço alvo do plano.
        if (filteredActions.length > 0) {
            int i = (int) Math.floor(Math.random() * filteredActions.length);
            // alterar para verdadeiro a flag que indica se tem um plano válido.
            hasActionToDo = true;
            // zera o contador de ações aleatórias, já que ele tem um plano para seguir.
            if (enoughOfRandom < 10) enoughOfRandom = 0;
            targetSpace.setX(filteredActions[i].getX());
            targetSpace.setY(filteredActions[i].getY());
        } else hasActionToDo = false;
    }

    private void doRandom() {
        // sempre que tentar algo aleatorio deve registrar no contador.
        enoughOfRandom++;
        // escolhe, aleatoriamente, uma ação apra executar.
        int i = (int) Math.floor(Math.random() * 5);
        switch (i) {
            case 0: doFoward(); break;
            case 1: doFoward(); break;
            case 2: doFoward(); break;
            case 3: doTurnRight(); break;
            case 4: doTurnLeft();
        }
    }

    private boolean shouldShutOffAtHome() {
        return ((enoughOfRandom >= 10) &&
            (currentSpace.getX() == homeSpace.getX()) &&
            (currentSpace.getY() == homeSpace.getY()));
    }

    private void goHomeAndShutOff() {
        // sabendo o endereço de casa, sempre define um plano que o leve para casa
        hasActionToDo = true;
        if (currentSpace.getX() > homeSpace.getX()) {
            targetSpace.setX(currentSpace.getX() - 1);
            targetSpace.setY(currentSpace.getY());
        } else if (currentSpace.getY() > homeSpace.getY()) {
            targetSpace.setX(currentSpace.getX());
            targetSpace.setY(currentSpace.getY() - 1);
        }
    }
}