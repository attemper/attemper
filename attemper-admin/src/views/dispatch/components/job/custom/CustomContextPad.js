export default class CustomContextPad {
  constructor(config, contextPad, create, elementFactory, injector, translate) {
    this.create = create
    this.elementFactory = elementFactory
    this.translate = translate

    if (config.autoPlace !== false) {
      this.autoPlace = injector.get('autoPlace', false)
    }

    contextPad.registerProvider(this)
  }

  getContextPadEntries(element) {
    const {
      autoPlace,
      create,
      elementFactory,
      translate
    } = this

    function append(event, element, key) {
      const shape = elementFactory.createShape({ type: 'bpmn:' + key })
      if (autoPlace) {
        autoPlace.append(element, shape)
      } else {
        create.start(event, shape, element)
      }
    }

    function appendStart(event, element, key) {
      const shape = elementFactory.createShape({ type: 'bpmn:' + key })

      create.start(event, shape, element)
    }

    // ParallelGateway
    function appendParallelGateway(event, element) {
      append(event, element, 'ParallelGateway')
    }

    function appendParallelGatewayStart(event) {
      appendStart(event, element, 'ParallelGateway')
    }

    // ServiceTask
    function appendServiceTask(event, element) {
      append(event, element, 'ServiceTask')
    }

    function appendServiceTaskStart(event) {
      appendStart(event, element, 'ServiceTask')
    }

    // ScriptTask
    function appendScriptTask(event, element) {
      append(event, element, 'ScriptTask')
    }

    function appendScriptTaskStart(event) {
      appendStart(event, element, 'ScriptTask')
    }

    return {
      'append.parallel-gateway': {
        group: 'model',
        className: 'bpmn-icon-gateway-parallel',
        title: translate('Append ParallelGateway'),
        action: {
          click: appendParallelGateway,
          dragstart: appendParallelGatewayStart
        }
      },
      'append.service-task': {
        group: 'model',
        className: 'bpmn-icon-service-task',
        title: translate('Append ServiceTask'),
        action: {
          click: appendServiceTask,
          dragstart: appendServiceTaskStart
        }
      },
      'append.script-task': {
        group: 'model',
        className: 'bpmn-icon-script-task',
        title: translate('Append ScriptTask'),
        action: {
          click: appendScriptTask,
          dragstart: appendScriptTaskStart
        }
      }
    }
  }
}

CustomContextPad.$inject = [
  'config',
  'contextPad',
  'create',
  'elementFactory',
  'injector',
  'translate'
]
