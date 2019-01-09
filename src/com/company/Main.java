package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Disp disp = new Disp();
        disp.setVisible(true);
    }
    static class Disp extends JFrame {
        static int turns;
        static boolean[][] arena;
        static int rx;
        static int ry;
        static JLabel winlabel;
        static JLabel rank1;
        static JLabel rank2;
        static JLabel rank3;
        static JLabel skelly;
        static JLabel skelly2;
        static boolean ingame;
        static boolean win;
        static boolean dbounce;
        public Disp() {
            init();
            addMouseListener(new Mouse(this));
        }
        public void init() {
            dbounce = true;
            turns = 0;
            setBounds(0,0,800,800);
            ingame = true;
            win = false;
            skelly = new JLabel(new ImageIcon("skelli.gif_c200"));
            skelly2 = new JLabel(new ImageIcon("skelli.gif_c200"));
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            arena = new boolean[15][15];
            ry = arena.length/2;
            rx = arena[0].length/2;
            skelly.setVisible(false);
            skelly2.setVisible(false);
            add(skelly);
            add(skelly2);
            Font font = new Font("Comic Sans MS", Font.PLAIN, 12);
            winlabel = new JLabel("U R WINNER");
            winlabel.setForeground(Color.BLUE);
            winlabel.setFont(font);
            add(winlabel);
            winlabel.setVisible(false);

            font = new Font("Comic Sans MS", Font.PLAIN, 9);
            rank1 = new JLabel("Empty");
            rank1.setForeground(Color.BLUE);
            rank1.setFont(font);
            add(rank1);
            rank1.setVisible(false);
            rank2 = new JLabel("Empty");
            rank2.setForeground(Color.BLUE);
            rank2.setFont(font);
            add(rank2);
            rank2.setVisible(false);
            rank3 = new JLabel("Empty");
            rank3.setForeground(Color.BLUE);
            rank3.setFont(font);
            add(rank3);
            rank3.setVisible(false);
        }
        public void paint(Graphics g) {
            super.paint(g);
            if(ingame) {
                for (int y = 0; y < arena.length; y++) {
                    for (int x = 0; x < arena[0].length; x++) {
                        if (arena[y][x]) {
                            g.setColor(Color.RED);
                            g.fillRect((int) ((x) * (getWidth() / arena[0].length)), (int) ((y) * (getHeight() / arena.length)), (getWidth() / arena[0].length), (getHeight() / arena.length));
                        }
                    }
                }
                g.setColor(Color.BLACK);
                g.fillRect((int) ((rx) * (getWidth() / arena[0].length)), (int) ((ry) * (getHeight() / arena.length)), (getWidth() / arena[0].length), (getHeight() / arena.length));
            }
            else if(win && dbounce) {
                setResizable(false);
                setBounds(0,0,250,150);
                skelly.setBounds(0, 0, 80, 150);
                skelly.setVisible(true);
                skelly2.setBounds(170, 0, 80, 150);
                skelly2.setVisible(true);
                winlabel.setBounds(83, 20, 90, 50);
                winlabel.setVisible(true);
                Scanner in = null;
                try {
                    in = new Scanner(new File("scores"));
                    ArrayList<String> topnames = new ArrayList<>();
                    ArrayList<Integer> topscores = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        String[] inp = in.nextLine().trim().split("\\.");
                        if (i == 0) {
                            topnames.add(inp[0]);
                            topscores.add(Integer.parseInt(inp[1]));
                        } else if (i == 1) {
                            if (Integer.parseInt(inp[1]) < topscores.get(0)) {
                                topnames.add(0, inp[0]);
                                topscores.add(0, Integer.parseInt(inp[1]));
                            } else {
                                topnames.add(inp[0]);
                                topscores.add(Integer.parseInt(inp[1]));
                            }
                        } else if (Integer.parseInt(inp[1]) < topscores.get(0)) {
                            topnames.add(0, inp[0]);
                            topscores.add(0, Integer.parseInt(inp[1]));
                        } else if (Integer.parseInt(inp[1]) < topscores.get(1)) {
                            topnames.add(1, inp[0]);
                            topscores.add(1, Integer.parseInt(inp[1]));
                        } else {
                            topnames.add(inp[0]);
                            topscores.add(Integer.parseInt(inp[1]));
                        }
                    }
                    int score = area(new boolean[arena.length][arena[0].length], rx, ry) * arena.length * arena[0].length;
                    int i = topscores.size() - 1;
                    while(i >= 0 && topscores.get(i) > score) {
                        i--;
                    }
                    topscores.add(i + 1, score);
                    topnames.add(i + 1, "Jacob");
                    rank1.setText("1. " + topnames.get(0) + " " + topscores.get(0));
                    rank1.setBounds(83, 40, 90, 50);
                    rank1.setVisible(true);
                    rank2.setText("2. " + topnames.get(1) + " " + topscores.get(1));
                    rank2.setBounds(83, 55, 90, 50);
                    rank2.setVisible(true);
                    rank3.setText("3. " + topnames.get(2) + " " + topscores.get(2));
                    rank3.setBounds(83, 70, 90, 50);
                    rank3.setVisible(true);
                    PrintWriter out = new PrintWriter(new File("scores"));
                    for(int x = 0; x < topnames.size(); x++) {
                        out.println(topnames.get(x) + "." + topscores.get(x));
                    }
                    out.flush();
                    out.close();
                    dbounce = false;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if(dbounce) {
                setResizable(false);
                setBounds(0,0,250,150);
                g.setColor(Color.RED);
                g.fillRect(0, 0, 250, 150);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                g.clearRect(0, 0, 250, 150);
                init();
            }
        }

        public static int area(boolean[][] visited, int x, int y) {
            if(x < 0 || y < 0 || x == visited[0].length || y == visited.length || visited[y][x]) {
                return 0;
            }
            visited[y][x] = true;
            return 1 + area(visited, x + 1, y)  + area(visited, x - 1, y)  + area(visited, x + 1, y + 1)  + area(visited, x + 1, y - 1)  + area(visited, x - 1, y + 1)  + area(visited, x - 1, y - 1)  + area(visited, x, y + 1)  + area(visited, x, y - 1);
        }

        public static int ratMove(int[][] visited, int x, int y, boolean origin) {
            if(x < 0 || y < 0 || x == visited[0].length || y == visited.length) return 0;
            if(arena[y][x]) {
                return visited.length * visited[0].length;
            }
            if(visited[y][x] != 0) return visited[y][x];
            visited[y][x] = visited.length * visited[0].length;
            int left = ratMove(visited, x - 1, y, false) + 1;
            int right = ratMove(visited, x + 1, y, false) + 1;
            int up = ratMove(visited, x, y - 1, false) + 1;
            int down = ratMove(visited, x, y + 1, false) + 1;
            int upleft = ratMove(visited, x - 1, y - 1, false) + 1;
            int upright = ratMove(visited, x + 1, y - 1, false) + 1;
            int downleft = ratMove(visited, x - 1, y + 1, false) + 1;
            int downright = ratMove(visited, x + 1, y + 1, false) + 1;
            int min;
            int dir;
            if(upleft > left || upleft > right || upleft > up || upleft > down || upleft > upright || upleft > downleft  || upleft > downright) {
                if (upright > left || upright > right || upright > up || upright > down || upright > downleft || upright > downright) {
                    if (downleft > left || downleft > right || downleft > up || downleft > down || upright > downright) {
                        if (downright > left || downright > right || downright > up || downright > down) {
                            if (right < left || up < left || down < left) {
                                if (up < right || down < right) {
                                    if (down < up) {
                                        min = down;
                                        dir = -1;
                                    } else {
                                        min = up;
                                        dir = 1;
                                    }
                                } else {
                                    min = right;
                                    dir = 2;
                                }
                            } else {
                                min = left;
                                dir = -2;
                            }
                        }
                        else {
                            min = downright;
                            dir = 11;
                        }
                    }
                    else {
                        min = downleft;
                        dir = 8;
                    }
                }
                else {
                    min = upright;
                    dir = 12;
                }
            }
            else {
                min = upleft;
                dir = 9;
            }
            if(min == visited.length * visited[0].length + 1 && origin) return 0;
            else if(min == 0 && origin) return 3;
            if(origin) {
                return dir;
            }
            visited[y][x] = min;
            return min;
        }

        class Mouse implements MouseListener {
            Disp dp;
            int watch = 0;
            public Mouse(Disp sp) {
                dp = sp;
            }
            public void mouseClicked(MouseEvent e) {
                watch++;
                int y = e.getY() / (dp.getHeight() / arena.length);
                int x = e.getX() / (dp.getWidth() / arena[0].length);
                if(x >= 0 && y >= 0 && x < arena[0].length && y < arena.length && !(x == rx && ry == y)) {
                    arena[y][x] = true;
                    turns++;
                    if (watch % 1 == 0) {
                        int dir = ratMove(new int[arena.length][arena[0].length], rx, ry, true);
                        if (rx < 0 || ry < 0 || rx == arena[0].length || ry == arena.length) {
                            win = false;
                            ingame = false;
                        } else if (dir == -2) rx--;
                        else if (dir == -1) ry++;
                        else if (dir == 1) ry--;
                        else if (dir == 2) rx++;
                        else if (dir == 8) {
                            ry++;
                            rx--;
                        } else if (dir == 9) {
                            ry--;
                            rx--;
                        } else if (dir == 11) {
                            ry++;
                            rx++;
                        } else if (dir == 12) {
                            ry--;
                            rx++;
                        } else if (dir == 0) {
                            win = true;
                            ingame = false;
                        }
                    }
                    dp.paint(dp.getGraphics());
                }
                else if(rx == x && ry == y) {
                    init();
                    paint(getGraphics());
                }
            }
            public void mousePressed(MouseEvent e) {

            }
            public void mouseReleased(MouseEvent e) {

            }
            public void mouseEntered(MouseEvent e) {
            }
            public void mouseExited(MouseEvent e) {
            }
        }
    }
}
