# Microsoft OAuth - Minecraft Authentication Bridge (plugin part)

## What is this?

For detailed description of this project head over to web repository for this project: [mic0ishere/microsoft-auth-bridge-web](https://github.com/mic0ishere/microsoft-auth-bridge-web)

This repository contains the plugin part of the project. It is responsible for letting people into the server.

## Configuration

A `config.yml` file will be created after the plugin gets loaded for the first time. Below is an explanation of the format.

### config.yml

```yaml
mongoURL: input your mongoDB URL here
bypassPlayers:
  - list of players that will be able to join the server without authentication
kickMessage: message that will be displayed to players that are not authenticated
```

## Building

Run `mvn clean package` to generate the plugin in the `target` directory.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
