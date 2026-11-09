package com.leivagaonahernanely.robloxhelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Catálogo de plantillas de scripts para Roblox Studio.
 * Todos los scripts son de desarrollo de juegos legítimos.
 */
public final class Templates {

    private Templates() {
    }

    public static final List<RobloxTemplate> TODAS = Collections.unmodifiableList(Arrays.asList(

            new RobloxTemplate(
                    "Puerta con botón",
                    "Una puerta que se abre y se cierra al usar un aviso de proximidad.",
                    "Script dentro de la Part que hará de puerta (Workspace).",
                    """
                            local puerta = script.Parent

                            local aviso = Instance.new("ProximityPrompt")
                            aviso.ActionText = "Abrir / cerrar"
                            aviso.ObjectText = "Puerta"
                            aviso.HoldDuration = 0
                            aviso.MaxActivationDistance = {{DISTANCIA}}
                            aviso.Parent = puerta

                            local abierta = false

                            aviso.Triggered:Connect(function(jugador)
                                abierta = not abierta
                                puerta.CanCollide = not abierta
                                puerta.Transparency = abierta and 0.7 or 0
                            end)
                            """,
                    "DISTANCIA", "8"),

            new RobloxTemplate(
                    "Monedas que suman",
                    "Recoge monedas, suma puntos y los muestra en la lista de jugadores.",
                    "Script en ServerScriptService. Crea partes llamadas 'Moneda' en el Workspace.",
                    """
                            local Players = game:GetService("Players")
                            local VALOR = {{VALOR_MONEDA}}

                            -- Crea la tabla leaderstats con "Monedas" para cada jugador que entra
                            Players.PlayerAdded:Connect(function(jugador)
                                local marcadores = Instance.new("Folder")
                                marcadores.Name = "leaderstats"
                                marcadores.Parent = jugador

                                local monedas = Instance.new("IntValue")
                                monedas.Name = "Monedas"
                                monedas.Value = 0
                                monedas.Parent = marcadores
                            end)

                            local function conectarMoneda(moneda)
                                local recogida = false
                                moneda.Touched:Connect(function(parte)
                                    if recogida or not parte.Parent then return end
                                    local jugador = Players:GetPlayerFromCharacter(parte.Parent)
                                    if not jugador then return end

                                    local stats = jugador:FindFirstChild("leaderstats")
                                    local monedas = stats and stats:FindFirstChild("Monedas")
                                    if not monedas then return end

                                    recogida = true
                                    monedas.Value += VALOR
                                    moneda:Destroy()
                                end)
                            end

                            -- Monedas que ya existen
                            for _, objeto in ipairs(workspace:GetDescendants()) do
                                if objeto.Name == "Moneda" and objeto:IsA("BasePart") then
                                    conectarMoneda(objeto)
                                end
                            end

                            -- Monedas que se añadan mientras se juega
                            workspace.DescendantAdded:Connect(function(objeto)
                                if objeto.Name == "Moneda" and objeto:IsA("BasePart") then
                                    conectarMoneda(objeto)
                                end
                            end)
                            """,
                    "VALOR_MONEDA", "1"),

            new RobloxTemplate(
                    "Lava que daña",
                    "Una zona que quita vida a quien la toca.",
                    "Script dentro de la Part de lava (Workspace).",
                    """
                            local parte = script.Parent
                            local DANIO = {{DANIO}}

                            parte.Touched:Connect(function(otra)
                                local humanoide = otra.Parent and otra.Parent:FindFirstChildOfClass("Humanoid")
                                if humanoide and humanoide.Health > 0 then
                                    humanoide:TakeDamage(DANIO)
                                end
                            end)
                            """,
                    "DANIO", "5"),

            new RobloxTemplate(
                    "Portal de teletransporte",
                    "Teletransporta al jugador a otra parte del mapa y evita el bucle al instante.",
                    "Script dentro de la Part del portal. Necesitas una Part con el nombre indicado en el Workspace.",
                    """
                            local Players = game:GetService("Players")
                            local portal = script.Parent
                            local destino = workspace:WaitForChild("{{NOMBRE_DESTINO}}")
                            local enEspera = {}

                            portal.Touched:Connect(function(otra)
                                local personaje = otra.Parent
                                if not personaje then return end

                                local jugador = Players:GetPlayerFromCharacter(personaje)
                                if not jugador or enEspera[jugador] then return end

                                local raiz = personaje:FindFirstChild("HumanoidRootPart")
                                if raiz then
                                    enEspera[jugador] = true
                                    raiz.CFrame = destino.CFrame + Vector3.new(0, 4, 0)
                                    task.delay(2, function()
                                        enEspera[jugador] = nil
                                    end)
                                end
                            end)
                            """,
                    "NOMBRE_DESTINO", "Destino"),

            new RobloxTemplate(
                    "Velocidad al aparecer",
                    "Fija la velocidad de caminar de cada jugador cada vez que aparece.",
                    "Script en ServerScriptService.",
                    """
                            local Players = game:GetService("Players")
                            local VELOCIDAD = {{VELOCIDAD}}

                            local function aplicarVelocidad(personaje)
                                local humanoide = personaje:WaitForChild("Humanoid")
                                humanoide.WalkSpeed = VELOCIDAD
                            end

                            Players.PlayerAdded:Connect(function(jugador)
                                jugador.CharacterAdded:Connect(aplicarVelocidad)
                                if jugador.Character then
                                    aplicarVelocidad(jugador.Character)
                                end
                            end)
                            """,
                    "VELOCIDAD", "24"),

            new RobloxTemplate(
                    "Botón en pantalla",
                    "Un botón de interfaz que cambia su texto al pulsarlo.",
                    "LocalScript dentro de un ScreenGui en StarterGui que contenga un TextButton llamado 'Boton'.",
                    """
                            local Players = game:GetService("Players")
                            local jugador = Players.LocalPlayer

                            local gui = script.Parent
                            local boton = gui:WaitForChild("Boton")

                            boton.Activated:Connect(function()
                                boton.Text = "{{TEXTO_BOTON}}"
                                print(jugador.Name .. " pulsó el botón")
                            end)
                            """,
                    "TEXTO_BOTON", "Pulsado")
    ));
}
