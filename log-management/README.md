# How to run project?

1. Using IDE:

    Right-click on your main application class and choose "Run" or "Debug."
2. Using Command Line:

    Open a terminal in your project directory. Run `./mvnw spring-boot:run`

# Running Prometheus, Grafana, and Alertmanager with Docker Compose
Navigate to the directory 'docker-log-management' containing Docker Compose configuration files and start the services:

```shell
cd docker-log-management
docker-compose up
```
This will download the necessary images and start the Prometheus, Grafana, and Alertmanager containers.
Once the containers are up and running, you can access the services using the following URLs:

* Prometheus: http://localhost:9090
* Grafana: http://localhost:3000
* Alertmanager: http://localhost:9093

### Accessing Grafana
1.  Open a web browser and navigate to the Grafana URL http://localhost:3000.
2.  Log in using the default credentials:  Username: `admin` Password: `admin`
3.  Inside Grafana, you can create data sources, import dashboards, and customize your monitoring setup.

# Guide to Sending Prometheus Notifications via Telegram
### Step 1: Create a Telegram Bot

1. Open Telegram and search for the "BotFather" bot or check **How Do I Create a Bot?** section [https://core.telegram.org/bots#6-botfather](https://core.telegram.org/bots#6-botfather)
2. Start a chat with BotFather and use the /newbot command to create a new bot
3. Follow the prompts to choose a name and username for your bot
4. BotFather will provide you with a unique bot token. Keep this token safe as it will be needed later

### Step 2: Get Chat ID for Your Channel

1. Create a Telegram channel (if you don't have one) and add your bot as an administrator
2. Send a message to your channel using your bot
3. Open a web browser and go to https://api.telegram.org/bot<your_bot_token>/getUpdates, replacing <your_bot_token> with your actual bot token
4. Look for the "chat" section in the JSON response. The "id" value in this section is your chat ID

### Step 3: Configure Alertmanager

Open the alertmanager.yml configuration file (/docker-log-management/alert-manager/config/alertmanager.yml).

Replace YOUR_BOT_TOKEN and YOUR_CHAT_ID with the actual values.

```yaml
telegram_configs:
  - bot_token: YOUR_BOT_TOKEN
    chat_id: YOUR_CHAT_ID
    api_url: https://api.telegram.org
    parse_mode: ''
```

### Step 4: Test the Configuration

1. Start or restart the Prometheus Alertmanager.
2. Open the Alertmanager web interface (usually at http://localhost:9093).
3. Click the "Status" tab and find the "Telegram" section.
4. Click the "Test" button next to your Telegram receiver. If everything is set up correctly, you should receive a test message in your Telegram channel.

Check the official documentation for up-to-date information: [https://prometheus.io/docs/alerting/latest/configuration/](https://prometheus.io/docs/alerting/latest/configuration/)