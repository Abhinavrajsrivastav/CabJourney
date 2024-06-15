import java.util.*;

public class CabJourney {

    private static final int MAX_CITIES = 20;
    private static final int INFINITY = Integer.MAX_VALUE;
    private static Map<String, Integer> cityIndexMap = new HashMap<>();
    private static List<String> cities = new ArrayList<>();
    private static int[][] distanceMatrix;
    private static int currentNumCities = 0;

    public static void main(String[] args) {
        initializeCities();
        initializeDistances();

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            showMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter your current location: ");
                    String currentCity = scanner.nextLine();
                    System.out.print("Enter your destination location: ");
                    String destinationCity = scanner.nextLine();
                    findShortestPath(currentCity, destinationCity);
                    break;
                case 2:
                    showAllDistances();
                    break;
                case 3:
                    findAllPairsShortestPaths();
                    break;
                case 4:
                    System.out.print("Enter the new locality name: ");
                    String newCity = scanner.nextLine();
                    addNewCity(newCity, scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice, please select again.");
            }
        } while (choice != 5);

        scanner.close();
    }

    private static void showMenu() {
        System.out.println("Main Menu:");
        System.out.println("1. Enter Location");
        System.out.println("2. Show Distance Between All Localities");
        System.out.println("3. Find All Pairs Shortest Paths");
        System.out.println("4. Add New Locality");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }

    private static void initializeCities() {
        // Initialize localities in Delhi
        addCity("Connaught Place");
        addCity("Karol Bagh");
        addCity("Lajpat Nagar");
        addCity("Dwarka");
        addCity("Saket");
        addCity("Rohini");
        addCity("Vasant Kunj");
        addCity("Noida Sector 18");
        addCity("Gurgaon");
        addCity("Ghaziabad");
        addCity("Janakpuri");
        addCity("Hauz Khas");
        addCity("Chandni Chowk");
        addCity("Rajouri Garden");
        addCity("Greater Kailash");
        addCity("Pitampura");
        addCity("Laxmi Nagar");
        addCity("Indirapuram");
        addCity("Mayur Vihar");
        addCity("Shahdara");

        distanceMatrix = new int[MAX_CITIES][MAX_CITIES];
        // Initialize distance matrix with INFINITY
        for (int i = 0; i < MAX_CITIES; i++) {
            Arrays.fill(distanceMatrix[i], INFINITY);
            distanceMatrix[i][i] = 0; // Distance to self is 0
        }
    }

    private static void addCity(String city) {
        if (currentNumCities < MAX_CITIES) {
            cities.add(city);
            cityIndexMap.put(city, currentNumCities);
            currentNumCities++;
        } else {
            System.out.println("Cannot add more cities. Maximum limit reached.");
        }
    }

    private static void initializeDistances() {
        // Add edges between localities with approximate distances in km
        addEdge("Connaught Place", "Karol Bagh", 5);
        addEdge("Connaught Place", "Lajpat Nagar", 10);
        addEdge("Karol Bagh", "Rohini", 15);
        addEdge("Lajpat Nagar", "Saket", 8);
        addEdge("Dwarka", "Vasant Kunj", 20);
        addEdge("Rohini", "Dwarka", 25);
        addEdge("Saket", "Noida Sector 18", 18);
        addEdge("Vasant Kunj", "Gurgaon", 15);
        addEdge("Noida Sector 18", "Ghaziabad", 12);
        addEdge("Gurgaon", "Ghaziabad", 30);
        addEdge("Connaught Place", "Janakpuri", 15);
        addEdge("Hauz Khas", "Chandni Chowk", 12);
        addEdge("Rajouri Garden", "Greater Kailash", 20);
        addEdge("Pitampura", "Laxmi Nagar", 18);
        addEdge("Indirapuram", "Mayur Vihar", 10);
        addEdge("Shahdara", "Laxmi Nagar", 8);
        addEdge("Rajouri Garden", "Dwarka", 12);
        addEdge("Saket", "Greater Kailash", 6);
        addEdge("Chandni Chowk", "Shahdara", 10);
        addEdge("Pitampura", "Rohini", 7);
    }

    private static void addEdge(String city1, String city2, int distance) {
        int index1 = cityIndexMap.get(city1);
        int index2 = cityIndexMap.get(city2);
        distanceMatrix[index1][index2] = distance;
        distanceMatrix[index2][index1] = distance; // Assuming undirected graph
    }

    private static void findShortestPath(String startCity, String endCity) {
        if (!cityIndexMap.containsKey(startCity) || !cityIndexMap.containsKey(endCity)) {
            System.out.println("Invalid city names.");
            return;
        }

        int startIndex = cityIndexMap.get(startCity);
        int endIndex = cityIndexMap.get(endCity);

        int[] distances = new int[currentNumCities];
        boolean[] visited = new boolean[currentNumCities];
        int[] prev = new int[currentNumCities];
        Arrays.fill(distances, INFINITY);
        Arrays.fill(prev, -1);

        distances[startIndex] = 0;

        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(city -> distances[city]));
        pq.add(startIndex);

        while (!pq.isEmpty()) {
            int currentCity = pq.poll();
            if (visited[currentCity]) continue;
            visited[currentCity] = true;

            for (int i = 0; i < currentNumCities; i++) {
                if (distanceMatrix[currentCity][i] != INFINITY && !visited[i]) {
                    int newDist = distances[currentCity] + distanceMatrix[currentCity][i];
                    if (newDist < distances[i]) {
                        distances[i] = newDist;
                        prev[i] = currentCity;
                        pq.add(i);
                    }
                }
            }
        }

        printPath(prev, startIndex, endIndex, distances[endIndex]);
    }

    private static void printPath(int[] prev, int startIndex, int endIndex, int totalDistance) {
        if (prev[endIndex] == -1) {
            System.out.println("No path found from " + cities.get(startIndex) + " to " + cities.get(endIndex));
            return;
        }

        List<String> path = new ArrayList<>();
        for (int at = endIndex; at != -1; at = prev[at]) {
            path.add(cities.get(at));
        }
        Collections.reverse(path);
        System.out.println("Shortest path: " + String.join(" -> ", path));
        System.out.println("Total distance: " + totalDistance + " km");
    }

    private static void showAllDistances() {
        System.out.println("Distances between all localities:");
        for (int i = 0; i < currentNumCities; i++) {
            for (int j = 0; j < currentNumCities; j++) {
                if (distanceMatrix[i][j] == INFINITY) {
                    System.out.print("INF ");
                } else {
                    System.out.print(distanceMatrix[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    private static void findAllPairsShortestPaths() {
        int[][] dist = new int[currentNumCities][currentNumCities];
        for (int i = 0; i < currentNumCities; i++) {
            for (int j = 0; j < currentNumCities; j++) {
                dist[i][j] = distanceMatrix[i][j];
            }
        }

        for (int k = 0; k < currentNumCities; k++) {
            for (int i = 0; i < currentNumCities; i++) {
                for (int j = 0; j < currentNumCities; j++) {
                    if (dist[i][k] != INFINITY && dist[k][j] != INFINITY && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        System.out.println("Shortest distances between all pairs of localities:");
        for (int i = 0; i < currentNumCities; i++) {
            for (int j = 0; j < currentNumCities; j++) {
                if (dist[i][j] == INFINITY) {
                    System.out.print("INF ");
                } else {
                    System.out.print(dist[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    private static void addNewCity(String newCity, Scanner scanner) {
        if (currentNumCities >= MAX_CITIES) {
            System.out.println("Cannot add more localities. Maximum limit reached.");
            return;
        }

        if (cityIndexMap.containsKey(newCity)) {
            System.out.println("Locality already exists.");
            return;
        }

        addCity(newCity);
        System.out.println("Enter distances from " + newCity + " to other localities (use -1 if no direct path):");
        for (int i = 0; i < currentNumCities - 1; i++) {
            System.out.print("Distance to " + cities.get(i) + ": ");
            int distance = scanner.nextInt();
            if (distance != -1) {
                addEdge(newCity, cities.get(i), distance);
            }
        }
    }
}
