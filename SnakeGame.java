import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

    private static final int GRID_SIZE = 20; // Kích thước lưới
    private static final int BOARD_SIZE = 600; // Kích thước của bảng
    private static final int DELAY = 100; // Thời gian chờ mỗi lần cập nhật (ms)

    private LinkedList<Point> snake;
    private Point food;
    private String direction;
    private boolean gameOver;
    private Timer timer;

    public SnakeGame() {
        this.snake = new LinkedList<>();
        this.snake.add(new Point(5, 5)); // Vị trí bắt đầu của rắn
        this.direction = "RIGHT"; // Hướng di chuyển ban đầu
        this.gameOver = false;

        // Tạo thức ăn cho rắn
        spawnFood();

        // Cài đặt Timer để cập nhật trò chơi
        this.timer = new Timer(DELAY, this);
        this.timer.start();

        // Cài đặt sự kiện bàn phím
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT && !direction.equals("RIGHT")) {
                    direction = "LEFT";
                } else if (key == KeyEvent.VK_RIGHT && !direction.equals("LEFT")) {
                    direction = "RIGHT";
                } else if (key == KeyEvent.VK_UP && !direction.equals("DOWN")) {
                    direction = "UP";
                } else if (key == KeyEvent.VK_DOWN && !direction.equals("UP")) {
                    direction = "DOWN";
                }
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            moveSnake();
            checkCollision();
            repaint();
        }
    }

    private void moveSnake() {
        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case "LEFT":
                newHead.x--;
                break;
            case "RIGHT":
                newHead.x++;
                break;
            case "UP":
                newHead.y--;
                break;
            case "DOWN":
                newHead.y++;
                break;
        }

        // Thêm đầu mới vào rắn
        snake.addFirst(newHead);

        // Kiểm tra nếu rắn ăn được thức ăn
        if (newHead.equals(food)) {
            spawnFood();
        } else {
            // Nếu không ăn thức ăn, bỏ đi phần đuôi rắn
            snake.removeLast();
        }
    }

    private void spawnFood() {
        Random rand = new Random();
        food = new Point(rand.nextInt(BOARD_SIZE / GRID_SIZE), rand.nextInt(BOARD_SIZE / GRID_SIZE));
    }

    private void checkCollision() {
        Point head = snake.getFirst();

        // Kiểm tra va chạm với biên của bảng
        if (head.x < 0 || head.x >= BOARD_SIZE / GRID_SIZE || head.y < 0 || head.y >= BOARD_SIZE / GRID_SIZE) {
            gameOver = true;
        }

        // Kiểm tra va chạm với chính thân rắn
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ bảng
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, BOARD_SIZE, BOARD_SIZE);

        // Vẽ rắn
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * GRID_SIZE, p.y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
        }

        // Vẽ thức ăn
        g.setColor(Color.RED);
        g.fillRect(food.x * GRID_SIZE, food.y * GRID_SIZE, GRID_SIZE, GRID_SIZE);

        // Vẽ thông báo game over
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", 200, BOARD_SIZE / 2);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(BOARD_SIZE, BOARD_SIZE);
        frame.add(game);
        frame.setVisible(true);
    }
}
