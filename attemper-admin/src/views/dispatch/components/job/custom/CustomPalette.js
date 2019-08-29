export default class CustomPalette {
  constructor(create, elementFactory, palette, translate) {
    this.create = create
    this.elementFactory = elementFactory
    this.translate = translate

    palette.registerProvider(this)
  }

  getPaletteEntries(element) {
    const {
      create,
      elementFactory,
      translate
    } = this

    function createShape(event, key) {
      const shape = elementFactory.createShape({ type: 'bpmn:' + key })

      create.start(event, shape)
    }

    function createParallelGateway(event) {
      createShape(event, 'ParallelGateway')
    }

    function createServiceTask(event) {
      createShape(event, 'ServiceTask')
    }

    function createScriptTask(event) {
      createShape(event, 'ScriptTask')
    }

    return {
      'append.parallel-gateway': {
        group: 'activity',
        className: 'bpmn-icon-gateway-parallel',
        title: translate('Append ParallelGateway'),
        action: {
          dragstart: createParallelGateway,
          click: createParallelGateway
        }
      },
      'create.service-task': {
        group: 'activity',
        className: 'bpmn-icon-service-task',
        title: translate('Create ServiceTask'),
        action: {
          dragstart: createServiceTask,
          click: createServiceTask
        }
      },
      'create.script-task': {
        group: 'activity',
        className: 'bpmn-icon-script-task',
        title: translate('Create ScriptTask'),
        action: {
          dragstart: createScriptTask,
          click: createScriptTask
        }
      }
    }
  }
}

CustomPalette.$inject = [
  'create',
  'elementFactory',
  'palette',
  'translate'
]
