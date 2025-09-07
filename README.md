# 📈 Stocky-Trades: Real-Time Trading Simulator (A-Level NEA Project)

> **Designed as an A-Level Computer Science NEA (2023–2024)**, Stocky Trades is a full-featured stock trading simulator built with JavaFX, using **live stock market data**, charting, API integration, database persistence, and modular OOP principles.

⚠️ **Note**: For full documentation oor the original 115-page technical report, [message me personally on LinkedIn](https://www.linkedin.com/in/aaryan-shariff/).

---

## 🚀 Project Summary

Stocky Trades is **not your typical A-Level project**.

Stocky Trades is a feature-rich **JavaFX** desktop application simulating a real-time stock trading platform.
Real-time stock trading simulator with JavaFX, API integration, and MySQL.
Unlike most NEA submissions (e.g., simple games or library systems), I went a step further — building a fully functional **stock trading simulation tool**, using **real financial data**, **live API calls**, and a custom-built GUI with **candlestick charts**, portfolio tracking, and a login system.

> The goal? Simulate the feel of trading in real-time — all coded from scratch using JavaFX and Gradle, and backed by MySQL

---

## 🌟 Key Features
- 🔐 Login system with MySQL + JDBC integration
- 📈 Real-time stock data via AlphaVantage API
- 💹 Candlestick charting with ChartFX
- 🗂️ Portfolio tracking + leaderboard
- 🧠 JSON parsing & search-as-you-type stock lookup
- 📉 Timeframe adjustable charting (1D, 1W, 1M, etc.)

## 🧠 Features at a Glance

| Feature | Description |
|--------|-------------|
| 🔐 **Login System** | User authentication using JDBC and MySQL,Real-time session tracking    |
| 🔍 **Search-as-you-type** | RESTFUL API Dynamic stock lookup with JSON parsing, Real-time **stock search bar** with suggestions per keystroke  |
| 📊 **Real-time Charts** | Advanced **candlestick charting** with ChartFX, Candlestick charts with time-frame adjustments |
| 📈 **Live Market Data** | Integration with AlphaVantage API |
| 🏆 **Leaderboard System** | Tracks top users based on virtual profits |
| 💼 **Portfolio View** | Tracks holdings and virtual account balances |
| 🔧 **Modular MVC Structure** | `Model`, `View`, and `Controller` packages for clean architecture |
| 📚 **Gradle + JavaFX Build** | Fully managed dependencies and structure for easy setup |

[AlphaVantage API](https://www.alphavantage.co/documentation/) 
---

## 📸 Screenshots

<img width="642" height="452" alt="stocky trades1" src="https://github.com/user-attachments/assets/fd42dcdf-c1cf-4e54-bd36-ffa4e56a5d0e" />
<img width="825" height="448" alt="stocky trades 4" src="https://github.com/user-attachments/assets/5b1cea96-8539-4593-9b5f-3a348b052688" />
<img width="812" height="451" alt="stocky trades 3" src="https://github.com/user-attachments/assets/79fa6892-0db5-4023-98d0-e96eab7347c9" />
<img width="773" height="456" alt="stocky trades 2" src="https://github.com/user-attachments/assets/4a9747ec-2ae0-42ae-99f9-3c5266ad73e7" />


---

## 🛠️ Tech Stack

| Category         | Tools/Tech Used                |
|------------------|-------------------------------|
| Language         | Java                          |
| UI Framework     | JavaFX                        |
| Database         | MySQL  + JDBC                       |
| API              | JSON + REST APIs AlphaVantage (for real-time data) |
| Charting Library | ChartFX / CandleFX            |
| Architecture     | MVC + Modular Java Structure  |

---
### ✅ Design Patterns Used

- **MVC Pattern** — Clean separation of logic/UI/data
- **Factory (for Charts)** — Handles various chart creation
- **Singleton** (planned for API rate limiting)


## 🧩 Incomplete (Due to Time Constraints)

| Feature | Status |
|--------|--------|
| Stock purchasing/selling logic | ❌ Incomplete backend logic |
| News Feed (Financial API integration) | ⏳ Not implemented |
| Error handling on failed API calls | ⚠️ Basic only |
| Final polish of trading UI | 🔧 Partially done |

---
---

## 🧠 Lessons Learned

- Overcomplicating a project may limit your ability to finish critical components.
- Time management and scope creep are just as important as technical skills.
- Real-world tools like APIs and charts taught me more than any basic NEA.
- Simpler, fully working projects often score better than ambitious but incomplete ones.

### 🔧 Requirements

- Java 17+
- Gradle (bundled with IntelliJ or CLI)
- JavaFX 17.0.2 SDK
- MySQL (local server or remote)
- Git

### 🏁 Getting Started

1. **Clone the Repository**

```bash
git clone https://github.com/arysgithub/Stocky-Trades
cd Stocky-Trades

2. **Open in IntelliJ IDEA**
    - Launch IntelliJ IDEA
    - Select 'Open' or `File -> Open` > select the cloned folder
    - Select `build.gradle` file
    - Click "Open as Project" and Select "Trust Project" if prompted
    - IntelliJ will automatically detect the Gradle build and import dependencies.

3. **Configure JDK**
    - Go to `File -> Project Structure`
    - Set SDK to JDK 17 or later
    - Set Language Level to match your JDK

4. **Build the Project**
    - Wait for Gradle to sync dependencies
    - Click Gradle refresh
    - Build using the Gradle task:
      ```bash
      ./gradlew build
      ```

5. **Run the Application**
    - Locate `Main.java` in the project explorer
    - Right-click and select "Run 'Main.main()'"
    - Alternatively, use the Gradle task:
      ```bash
      ./gradlew run
      ```

---

##🏆 Why This Project Stands Out

🧠 Designed beyond curriculum level
🕵️‍♂️ Involves financial data, charting, API integration
💾 Uses database-backed login systems
🎯 Reflects real-world concepts: latency, API quotas, DB sync
🎓 First step into DevOps/Cloud readiness — built using CLI, Git, Gradle, modular folders

## License
Licensed under the MIT License
