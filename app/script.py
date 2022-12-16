# coding=utf-8

import json
from datetime import datetime

from definitions.geneticAlgorithm import PopulationGenerator, NaturalSelection
from definitions.presentation import Graphics

# Configurações
population_size = 100
mutation_rate = 0.1
arena_size = 40
graphics_width = 15
graphics_height = 8
generations = 25
routes_to_plot = 1

# Leitura de arquivos
with open("data/distances.json", "r") as read_file:
    distance = json.load(read_file)
with open("data/coordinates.json", "r") as read_file:
    coordinates = json.load(read_file)
with open("data/names.json", "r") as read_file:
    name = json.load(read_file)

melhores_resultado = []

for a in range(10):

    melhor_cost = 0
    melhor_result = {}

    for i in range(10):

        # Pega lista de cidades
        city_list = list(coordinates.keys())

        # População inicial
        initial_population = PopulationGenerator(population_size, city_list).generatePopulation()

        # Inicia os modelos de lógica e apresentação
        model = NaturalSelection(distance, initial_population)
        graphics = Graphics(routes_to_plot, generations, coordinates, name, graphics_width, graphics_height)

        # Liga o modelo de apresentação ao lógico, para que ele receba os relatório de progresso
        model.subscribe(graphics.receiveData)

        # Salva o custo antigo
        old_cost = model.getFitness()[model.getFittest()]

        # Mostra a rota e custo inicial
        old_fittest = model.population[model.getFittest()]
        print('\nO melhor indivíduo da geração inicial faz a rota: ', graphics.describeRoute(old_fittest),
              '\nSeu custo é: ', old_cost,
              '\nRota em lista: ', old_fittest)

        # Executa a evolução
        the_fittest = model.geneticAlgorithm(generations, arena_size, mutation_rate, True)

        # Gera o gráfico
        # graphics.generateGraph()

        # Pega o custo final do modelo
        final_cost = model.getFitness()[model.getFittest()]

        # Mostra a rota e custo final
        print('\nO melhor indivíduo da geração final faz a rota: ', graphics.describeRoute(the_fittest),
              '\nSeu custo é: ', final_cost,
              '\nRota em lista: ', the_fittest)

        print('\n---------------------------------------------------------------------------------------')

        if melhor_cost == 0 or final_cost < melhor_cost:
            melhor_result = {
                'cost': final_cost,
                'data': the_fittest
            }

    melhores_resultado.append(melhor_result)
    # graphics.display()

total_cost = 0
qtde_cost = 0
for result in melhores_resultado:
    total_cost += result['cost']
    qtde_cost += 1

print('\nForam encontrados ', str(qtde_cost), 'melhores resultados',
      '\nSeus custos somados foram ', str(total_cost),
      '\nE a média é de ', str(total_cost/qtde_cost))

f = open("/home/luiz/Documentos/result_" + str(datetime.timestamp(datetime.now())) + ".json", "w")
f.write(json.dumps(melhores_resultado, indent=2))
f.close()
