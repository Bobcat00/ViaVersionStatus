# ViaVersionStatus

[![SpigotMC](https://img.shields.io/badge/SpigotMC-ViaVersionStatus-orange)](https://www.spigotmc.org/resources/viaversionstatus.66959/)
[![bStats](https://img.shields.io/badge/bStats-Metrics-blue)](https://bstats.org/plugin/bukkit/ViaVersionStatus)

ViaVersionStatus is a lightweight plugin for [Spigot/Paper servers](https://www.spigotmc.org/) that reports which **Minecraft client versions** your players are using when they join your server.  

It provides:
- Detailed version info in the **server log**  
- Real-time **in-game notifications** for staff/admins  
- Optional **warnings to players** if their client version doesn’t match the server’s version  
- Ability to **trigger console commands** based on client version  
- Optional **Prism logging integration**  

---

## ✨ Features
- Notify staff about player client versions on join
- Warn players if they join with newer/older versions than the server
- Trigger console commands depending on version
- Prism logging support (`vvs-client-connect` action)
- Metrics support via [bStats](https://bstats.org/plugin/bukkit/ViaVersionStatus)
- Works with **ViaVersion**, **ViaBackwards**, and **ViaRewind**

---

## 📦 Requirements
- Java **17+**
- [ViaVersion](https://www.spigotmc.org/resources/viaversion.19254/) (required)  
- Spigot, Paper, or any Bukkit-compatible server software

Optional:
- [Prism](https://www.spigotmc.org/resources/prism.75166/) (for logging integration)

---

## ⚙️ Installation
1. Install [ViaVersion](https://www.spigotmc.org/resources/viaversion.19254/) on your server.  
2. Download the latest **ViaVersionStatus.jar** from [SpigotMC](https://www.spigotmc.org/resources/viaversionstatus.66959/) or build from source.  
3. Place the `.jar` into your `plugins/` folder.  
4. Restart the server.  
5. (Optional) Edit the `config.yml` to customize messages and behavior.

---

## 🛠️ Configuration

The config file (`plugins/ViaVersionStatus/config.yml`) comes with sensible defaults.  

Available options include:
- `notify-ops` – Notify server operators by default  
- `notify-string` – Message sent to staff when a player joins  
- `notify-command` – Console command triggered when a player joins  
- `warn-players` – Warn players if using an older version  
- `warn-players-newer` – Warn players if using a newer version  
- `warn-string`, `warn-string-newer` – Warning messages  
- `warn-command`, `warn-command-newer` – Commands triggered on version mismatch  
- `high-priority` – Adjust execution priority if `%displayname%` doesn’t resolve properly  
- `list-supported-protocols` – Log supported protocols at startup  
- `block-no-light-data-warnings` – Hide “No light data found” spam (advanced)  
- `enable-metrics` – Enable/disable bStats metrics  
- `prism-integration` – Enable Prism logging integration  

Placeholders you can use in strings:  
- `%player%` → Player’s username  
- `%displayname%` → Player’s display name  
- `%version%` → Player’s client version  
- `%server%` → Server version  

---

## 🔑 Permissions

| Permission                                   | Description                                                                 | Default |
|----------------------------------------------|-----------------------------------------------------------------------------|---------|
| `viaversionstatus.notify`                    | Receive version join notifications                                          | OP      |
| `viaversionstatus.notify.ignoresame`         | Ignore notifications when client matches server version                     | false   |
| `viaversionstatus.exempt`                    | Exempts player from **all processing**                                      | false   |
| `viaversionstatus.exempt.log`                | Exempts from logging                                                        | false   |
| `viaversionstatus.exempt.notify`             | Exempts from triggering notifications                                       | false   |
| `viaversionstatus.exempt.notify.message`     | Exempts from sending notification messages                                  | false   |
| `viaversionstatus.exempt.notify.command`     | Exempts from triggering notification commands                               | false   |
| `viaversionstatus.exempt.warn`               | Exempts from all warnings                                                   | false   |
| `viaversionstatus.exempt.warn.message`       | Exempts from receiving warning messages                                     | false   |
| `viaversionstatus.exempt.warn.command`       | Exempts from triggering warning commands                                    | false   |
| `viaversionstatus.exempt.warn.newer`         | Exempts from all newer-version warnings                                     | false   |
| `viaversionstatus.exempt.warn.newer.message` | Exempts from receiving newer-version warnings                               | false   |
| `viaversionstatus.exempt.warn.newer.command` | Exempts from triggering newer-version warning commands                      | false   |

> **Note:** Usually only `viaversionstatus.notify` is required for admins/staff.  

---

## 📜 Commands
This plugin **does not add any commands**.  
Use `/viaversion list` from ViaVersion to check online player versions.

---

## 📓 Prism Integration
If [Prism](https://www.spigotmc.org/resources/prism.75166/) is installed and integration enabled:  
- Each join event logs the player’s client version as `vvs-client-connect`.  
- Example Prism config snippet:  
```yaml
api:
  enabled: true
  allowed-plugins:
    - ViaVersionStatus
```

---

## 🖥️ Compiling from Source
To build the plugin yourself:
1. Clone the repository:  
   ```bash
   git clone https://github.com/Bobcat00/ViaVersionStatus.git
   cd ViaVersionStatus
   ```
2. Run Maven to compile:  
   ```bash
   mvn clean package
   ```
3. The compiled `.jar` will be in the `target/` folder.

---

## 📊 Metrics
This plugin uses [bStats](https://bstats.org/) to collect **anonymous usage data**.  
You can opt-out at any time by setting `enable-metrics: false` in the config.

---

## 📎 Links
- 📥 [Download on SpigotMC](https://www.spigotmc.org/resources/viaversionstatus.66959/)  
- 💻 [Source Code on GitHub](https://github.com/Bobcat00/ViaVersionStatus)  
- 📊 [bStats Metrics](https://bstats.org/plugin/bukkit/ViaVersionStatus)  

